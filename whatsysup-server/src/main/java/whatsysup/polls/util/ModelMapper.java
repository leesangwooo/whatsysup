/*
 * Decompiled with CFR 0_123.
 * 
 * Could not load the following classes:
 *  com.google.gson.Gson
 *  com.google.gson.GsonBuilder
 *  whatsysup.polls.model.Choice
 *  whatsysup.polls.model.CpuTop
 *  whatsysup.polls.model.JStack
 *  whatsysup.polls.model.Poll
 *  whatsysup.polls.model.User
 *  whatsysup.polls.payload.ChoiceResponse
 *  whatsysup.polls.payload.PollResponse
 *  whatsysup.polls.payload.UserSummary
 */
package whatsysup.polls.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.lang.invoke.LambdaMetafactory;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.function.ToLongFunction;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.LongStream;
import java.util.stream.Stream;
import whatsysup.polls.model.Choice;
import whatsysup.polls.model.CpuTop;
import whatsysup.polls.model.JStack;
import whatsysup.polls.model.Poll;
import whatsysup.polls.model.User;
import whatsysup.polls.payload.ChoiceResponse;
import whatsysup.polls.payload.PollResponse;
import whatsysup.polls.payload.UserSummary;

public class ModelMapper {
    public static PollResponse mapPollToPollResponse(Poll poll, Map<Long, Long> choiceVotesMap, User creator, Long userVote) {
        PollResponse pollResponse = new PollResponse();
        pollResponse.setId(poll.getId());
        pollResponse.setQuestion(poll.getQuestion());
        pollResponse.setCreationDateTime(poll.getCreatedAt());
        pollResponse.setExpirationDateTime(poll.getExpirationDateTime());
        pollResponse.setTags(poll.getTags());
        Instant now = Instant.now();
        pollResponse.setExpired(Boolean.valueOf(poll.getExpirationDateTime().isBefore(now)));
        List choiceResponses = poll.getChoices().stream().map(choice -> {
            ChoiceResponse choiceResponse = new ChoiceResponse();
            choiceResponse.setId(choice.getId().longValue());
            choiceResponse.setText(choice.getText());
            if (choiceVotesMap.containsKey(choice.getId())) {
                choiceResponse.setVoteCount(((Long)choiceVotesMap.get(choice.getId())).longValue());
            } else {
                choiceResponse.setVoteCount(0);
            }
            return choiceResponse;
        }
        ).collect(Collectors.toList());
        pollResponse.setChoices(choiceResponses);
        UserSummary creatorSummary = new UserSummary(creator.getId(), creator.getUsername(), creator.getName());
        pollResponse.setCreatedBy(creatorSummary);
        if (userVote != null) {
            pollResponse.setSelectedChoice(userVote);
        }
        long totalVotes = pollResponse.getChoices().stream().mapToLong(ChoiceResponse::getVoteCount).sum();
        pollResponse.setTotalVotes(Long.valueOf(totalVotes));
        return pollResponse;
    }

    public static Map<String, Object> gsonResponseMapper(String jsonString, String message) {
        HashMap<String, Object> response = new HashMap<String, Object>();
        Gson gson = new GsonBuilder().create();
        Map gsonMap = (Map)gson.fromJson(jsonString, Map.class);
        response.put("maps", gsonMap);
        response.put("keys", gson.toJson((Object)gsonMap.keySet().toArray(new String[gsonMap.size()])));
        response.put("values", gson.toJson((Object)gsonMap.values().toArray(new String[gsonMap.size()])));
        response.put("length", gsonMap.size());
        response.put("message", message);
        return response;
    }

    public static JStack jstackResponseMapper(Long userId, Long pollId, String origin) {
        HashMap<String, String> map = new HashMap<String, String>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Matcher matcher = null;
        Pattern spiltPtrn = Pattern.compile("\n\n|\r\r");
        Pattern jsonParsePtrn = Pattern.compile("\\\"(.*)\\\"|(\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2})");
        long key = 0;
        String[] split = spiltPtrn.split(origin);
        int index = 0;
        String[] arrstring = split;
        int n = arrstring.length;
        int n2 = 0;
        while (n2 < n) {
            String element = arrstring[n2];
            matcher = jsonParsePtrn.matcher(element = element.replaceAll("[\\.]", "_"));
            if (matcher.find()) {
                if (index == 0) {
                    try {
                        key = dateFormat.parse(matcher.group()).getTime() - 32400000;
                    }
                    catch (ParseException e) {
                        e.printStackTrace();
                        break;
                    }
                }
                map.put(matcher.group(), element);
                ++index;
            }
            ++n2;
        }
        return new JStack(Long.valueOf(key), userId, pollId, map);
    }

    public static CpuTop cpuTopResponseMapper(Long userId, Long pollId, String origin) {
        HashMap values = new HashMap();
        HashMap<String, String> headerMap = new HashMap<String, String>();
        HashMap<String, Object> topNMap = new HashMap<String, Object>();
        values.put("headers", headerMap);
        values.put("topN", topNMap);
        String[] block = origin.split("\n\n|\r\r");
        String[] arrstring = block[0].split("\n|\r");
        int n = arrstring.length;
        int n2 = 0;
        while (n2 < n) {
            String headerLine = arrstring[n2];
            String[] temp = headerLine.split(":");
            headerMap.put(temp[0], temp[1].replaceAll("[\\.]", ""));
            ++n2;
        }
        String[] topValues = block[1].split("\n|\r");
        String[] nameArr = topValues[0].split("\\s");
        topNMap.put("topN_headers", nameArr);
        int rank = 1;
        while (rank < topValues.length) {
            String[] valueArr = topValues[rank].split("\\s");
            HashMap<String, String> topNPairs = new HashMap<String, String>();
            int i = 0;
            while (i < nameArr.length) {
                topNPairs.put(nameArr[i], valueArr[i]);
                ++i;
            }
            topNMap.put(String.valueOf(rank), topNPairs);
            ++rank;
        }
        return new CpuTop(Long.valueOf(new Date().getTime()), userId, pollId, values);
    }
}
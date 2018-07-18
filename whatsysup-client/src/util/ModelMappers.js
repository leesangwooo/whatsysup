export function cpuHeadersMapper(res) {
    if (Array.isArray(res) && res.length) {
        const result = [];
        res.map(item => {
            let temp = item.values.headers;
            result.push({
                key: item.timeKey,
                value: temp,
            });
            return null;
        });
        return result;
    }
}
export function cpuTopMapper(res) {
    if (Array.isArray(res) && res.length) {
        const result = [];
        res.map(item => {
            let temp = item.values.topN;
            result.push({
                key: item.timeKey,
                value: temp,
            });
            return null;
        });
        //console.log(result)
        return result;
    }
}
export function cpuUsageMapper(res) {
    if (Array.isArray(res) && res.length) {
        const result = [];
        res.map(item => {
            let temp = item.values.headers['CPU usage'];
            let val = temp.match(/\d+/g);
            result.push({
                x: item.timeKey,
                y1: val[0] * 1,
                y2: val[1] * 1,
                y3: val[2] * 1,
            });
            return null;
        });
        return result;
    }
}

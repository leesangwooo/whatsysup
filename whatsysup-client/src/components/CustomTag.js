import React, { Component } from 'react';
import { Input, Icon, Tag, Tooltip } from 'antd';

class CustomTag extends Component {
    constructor(props) {
        super(props);
        this.state = {
            tags: [],
            tagKeys: [],
            tagMaps: props.tagMaps,
            tagInputVisible: false,
            tagInputValue: '',
            readOnly: props.readOnly || false,
        };
    }

    onChangeTagMaps(tagMaps) {
        this.props.onChange(tagMaps);
    }

    componentWillReceiveProps(nextProps) {
        this.setState({
            tags: Object.values(nextProps.tagMaps),
            tagKeys: Object.keys(nextProps.tagMaps),
            tagMaps: nextProps.tagMaps,
        });
    }

    handleTagClose = removedTag => {
        const tagMaps = this.state.tagMaps;
        Object.values(this.state.tagMaps).map((val, index) => {
            if (val === removedTag) {
                delete tagMaps[Object.keys(tagMaps)[index]];
            }
            return null;
        });
        this.onChangeTagMaps(tagMaps);
        this.setState({ tagMaps });
    };

    showTagInput = () => {
        this.setState({ tagInputVisible: true }, () => this.input.focus());
    };

    handleTagInputChange = e => {
        this.setState({ tagInputValue: e.target.value });
    };

    handleTagInputConfirm = () => {
        const { tagInputValue, tagMaps } = this.state;
        if (tagInputValue && Object.keys(tagMaps).indexOf(tagInputValue) === -1) {
            let newKey = '*' + Object.keys(tagMaps).length;
            tagMaps[newKey] = tagInputValue;
        }
        this.onChangeTagMaps(tagMaps);
        this.setState({
            tagMaps,
            tagInputVisible: false,
            tagInputValue: '',
        });
    };
    saveInputRef = input => (this.input = input);

    render() {
        const { tagMaps, tagInputVisible, tagInputValue, readOnly } = this.state;
        const tags = Object.values(tagMaps),
            tagKeys = Object.keys(tagMaps);
        const { color } = this.props;

        return (
            <div>
                {tags.map((tag, index) => {
                    const isLongTag = tag.length > 20 || tag.length === 0;
                    const tagElem = (
                        <Tooltip title={tagKeys[index] + ' : ' + tag} key={tag + '-' + index}>
                            <Tag
                                key={tag + '-' + index}
                                closable={!readOnly}
                                afterClose={() => this.handleTagClose(tag)}
                                color={color}
                                style={{ margin: 8, fontSize: '1em' }}
                            >
                                {isLongTag ? (
                                    <span>
                                        {tag.slice(0, 20)}
                                        <Icon type="ellipsis" />
                                    </span>
                                ) : (
                                    tag
                                )}
                            </Tag>
                        </Tooltip>
                    );
                    return isLongTag ? (
                        <Tooltip title={tagKeys[index] + ' : ' + tag} key={tag + '-' + index}>
                            {tagElem}
                        </Tooltip>
                    ) : (
                        tagElem
                    );
                })}
                {tagInputVisible && (
                    <Input
                        ref={this.saveInputRef}
                        type="text"
                        size="small"
                        style={{ width: 78 }}
                        value={tagInputValue}
                        onChange={this.handleTagInputChange}
                        onBlur={this.handleTagInputConfirm}
                        onPressEnter={this.handleTagInputConfirm}
                    />
                )}
                {!tagInputVisible &&
                    !readOnly && (
                        <Tag
                            onClick={this.showTagInput}
                            style={{ background: '#fff', borderStyle: 'dashed', fontSize: '1em' }}
                            color="blue"
                        >
                            <Icon type="plus" /> New Tag
                        </Tag>
                    )}
            </div>
        );
    }
}
export default CustomTag;

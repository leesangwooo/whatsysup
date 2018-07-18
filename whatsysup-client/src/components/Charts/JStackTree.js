import React, { Component } from 'react';
import { Tree, Modal, Tag, Icon } from 'antd';
import { getStackTrace } from '../../util/APIUtils';
import { notification } from 'antd/lib/index';
import moment from 'moment';
import _ from 'lodash';
//import HighlightText from 'react-highlight-text';

const TreeNode = Tree.TreeNode;

class JStackTree extends Component {
    constructor(props) {
        super(props);
        this.state = {
            pollId: props.pollId,
            startDate: props.startDate,
            endDate: props.endDate,
            datas: [],
            selectedData: '',
        };
        this.createDomUseData.bind(this);
    }
    onSelect = (selectedKeys, info) => {
        //console.log(info.node.props.data)
        if (selectedKeys[0].split(/-/).length === 3) {
            let selectedData = info.node.props.data;
            this.showInfoModal(selectedData);
            this.setState({
                selectedData: selectedData,
            });
        } else {
            return null;
        }
    };
    fetchData() {
        const { pollId, startDate, endDate } = this.state;
        if (pollId && startDate && endDate) {
            getStackTrace(pollId, startDate, endDate)
                .then(resp => {
                    // sort.. not necessary now
                    // resp.map((val, index) => {
                    //     let ordered = {};
                    //     Object.keys(val.values)
                    //         .sort()
                    //         .map(sortedKey => {
                    //             ordered[sortedKey] = val.values[sortedKey];
                    //         });
                    //     resp[index].values = ordered;
                    // });

                    //console.log(temp, resp)
                    this.setState({
                        datas: resp,
                    });
                })
                .catch(error => {
                    notification.error({
                        message: 'Whathef***!',
                        description: error.message || 'Agent is not working... Please try again!',
                    });
                });
        }
    }

    createDomUseData = () => {
        const { datas } = this.state;

        let valid = false;
        let rootElements = [];
        if (datas) {
            datas.map((item, rootIndex) => {
                let rootKey = moment(item.timeKey).format('YYYY-MM-DD ddd, HH:mm:ss');
                let childElements = [];
                let groups = Object.keys(item.values).map(function(key) {
                    return {
                        key: key.split(/\s|-/)[0],
                        value: item.values[key],
                    };
                });
                // group by its name tokens
                groups = _.groupBy(groups, 'key');

                Object.keys(groups).map((title, childIndex) => {
                    //console.log(title, groups[title])
                    let subChildElements = [];
                    groups[title].map((item, subChildIndex) => {
                        subChildElements.push(
                            <TreeNode
                                data={item.value}
                                title={item.value.match(/([^\\"]|\\"$)+/)}
                                key={rootIndex + '-' + childIndex + '-' + subChildIndex}
                            />
                        );

                    });
                    return childElements.push(
                        <TreeNode
                            title={
                                <span>
                                    {title.substring(title.indexOf('"') + 1)}&nbsp;&nbsp;
                                    <Tag
                                        style={{
                                            opacity: 0.5,
                                            background: '#827f96',
                                            color: '#ffffff',
                                        }}
                                    >
                                        {groups[title].length}
                                    </Tag>
                                </span>
                            }
                            key={rootIndex + '-' + childIndex}
                        >
                            {subChildElements}
                        </TreeNode>
                    );
                });
                rootElements.push(
                    <TreeNode
                        title={
                            <span>
                                {rootKey}&nbsp;&nbsp;<Tag style={{ opacity: 0.5 }}>
                                    {Object.keys(item.values).length}
                                </Tag>
                            </span>
                        }
                        key={rootIndex}
                    >
                        {childElements}
                    </TreeNode>
                );
                return (valid = true);
            });
        }
        if (!valid) {
            return null;
        }
        return (
            <Tree showLine defaultExpandedKeys={['0-0-0']} onSelect={this.onSelect}>
                {rootElements}
            </Tree>
        );
    };

    showInfoModal(selectedData) {
        //console.log(selectedData)
        const fullWidth = window.innerWidth;
        /**
         * @TODO HIGHLIGHT CSS
        _.replace(selectedData, /(at\s)|(\(.*\))/g, function(a, b){
            return <span style={{color: '#4256f4'}}>{b}</span>
        })
        */
        Modal.info({
            title: 'Java Stack Details',
            width: fullWidth,
            content: (
                <pre style={{ width: fullWidth * 0.9, overflow: 'wrap', paddingBottom: 20 }}>
                    {selectedData}
                </pre>
            ),
            onOk() {},
        });
    }

    componentDidMount() {
        this.fetchData();
    }

    render() {
        return <div>{this.createDomUseData() || <Icon type="meh-o" style={{ width: '100%',margin: '0 auto',fontSize: 16, color: '#08c' }} spin={true} > No data</Icon>}</div>;
    }
}
export default JStackTree;

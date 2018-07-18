import React, { Component } from 'react';
import { notification as noti } from 'antd/lib/index';
import TimelineChart from './AntTimeline';
import StackTree from './JStackTree';
import CpuTopTable from './CpuTopTable';
import { Divider, Tooltip, Tabs, Icon } from 'antd';
import { getUserCpuTops } from '../../util/APIUtils';
import { cpuHeadersMapper, cpuTopMapper, cpuUsageMapper } from '../../util/ModelMappers';
const TabPane = Tabs.TabPane;
/*
const openNotification = () => {
    const args = {
        type: 'info',
        placement: 'topRight',
        top: 100,
        message: 'Notification Title',
        description:
            'test',
        duration: 0,
    };
    notification.open(args);
};
*/
class Index extends Component {
    constructor(props) {
        super(props);

        this.state = {
            pollId: props.poll.id,
            cpuHeaders: {},
            cpuTop: [],
            cpuUsageData: [],
            isMouseEnter: false,
            isMouseUp: true,
            startDate: new Date(props.poll.creationDateTime).getTime(),
            endDate: new Date().getTime(),
        };

        this.loadData.bind(this);
    }
    loadData = (pollId, startDate, endDate) => {
        getUserCpuTops(pollId, startDate, endDate)
            .then(response => {
                this.setState({
                    cpuHeaders: cpuHeadersMapper(response),
                    cpuTop: cpuTopMapper(response),
                    cpuUsageData: cpuUsageMapper(response),
                });
            })
            .catch(error => {
                console.log(error)
            });
    };

    handleCallbackFn = (startDate, endDate) => {
        // update by tick performance
        let tick = 1000 * 3;
        if (
            Math.abs(this.state.startDate - startDate) > tick ||
            Math.abs(this.state.endDate - endDate) > tick
        ) {
        }
    };

    componentDidMount() {
        const { pollId, startDate, endDate } = this.state;
        this.loadData(pollId, startDate, endDate);
    }

    render() {
        const { cpuUsageData, cpuHeaders, cpuTop, startDate, endDate } = this.state;
        return (
            <div>
                <Divider orientation={'left'}>CPU Monitor</Divider>
                <div>
                    <Tooltip placement="top" title={'This Question\'s Agent was FAILED...'}>
                        {Array.isArray(cpuUsageData) && cpuUsageData.length ? (
                            <TimelineChart
                                height={300}
                                data={cpuUsageData}
                                titleMap={{
                                    y1: 'User',
                                    y2: 'System',
                                    y3: 'Idle',
                                }}
                                callbackFn={this.handleCallbackFn}
                                cpuHeaders={cpuHeaders}
                                cpuTop={cpuTop}
                            />
                        ) : (
                            <Icon type="meh-o" style={{ width: '100%',margin: '0 auto',fontSize: 16, color: '#08c' }} spin={true} > No data</Icon>
                        )}
                    </Tooltip>
                </div>
                <div />
                <Divider dashed={true} />
                <div>
                    <Tabs defaultActiveKey="1">
                        <TabPane
                            tab="Java Stack"
                            key="1"
                            style={{ minHeight: 80, maxHeight:370, overflow: 'scroll' }}
                        >
                            <StackTree
                                pollId={this.state.pollId}
                                startDate={startDate}
                                endDate={endDate}
                            />
                        </TabPane>
                        <TabPane
                            tab="TOP CPU Usage"
                            key="2"
                            style={{ minHeight: 80, maxHeight:370, overflow: 'auto' }}
                        >
                            <CpuTopTable topNDatas={this.state.cpuTop} />
                        </TabPane>
                    </Tabs>
                </div>
                <Divider dashed={true} />
            </div>
        );
    }
}
export default Index;

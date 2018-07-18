import React, { Component } from 'react';
import { Table } from 'antd';
import { generateId } from '../../util/Helpers';

class CpuTopTable extends Component {
    constructor(props) {
        super(props);

        this.state = {
            columns: [],
            datas: [],
            topNDatas: props.topNDatas,
            loading: false,
        };
    }
    dataTableMapper = (topNDatas, timeKey) => {
        const columns = [];
        const datas = [];
        if (topNDatas && topNDatas[0].hasOwnProperty('key')) {
            // set column
            let columnArr = topNDatas[0].value.topN_headers;
            columnArr.map((title, index) => {
                let column = { title: title, dataIndex: title, key: index, width: 130 };
                return columns.push(column);
            });

            // fixed columns
            for (let i = 0; i < 2; i++) {
                columns[i] = {
                    ...columns[i],
                    width: 95,
                    fixed: 'left',
                };
            }
            // set values
            topNDatas.map(topNData => {
                for (let rank = 1; rank <= 5; rank++) {
                    datas.push({
                        key: generateId(),
                        ...topNData.value[rank],
                    });
                }
                return null;
            });
        }
        this.setState({
            columns: columns,
            datas: datas,
        });
    };
    componentDidMount() {
        this.dataTableMapper(this.state.topNDatas);
    }

    render() {
        return (
            <Table
                columns={this.state.columns}
                size={'small'}
                dataSource={this.state.datas}
                pagination={{ pageSize: 5 }}
                indentSize={10}
                scroll={{ x: 4900, y: 300 }}
                loading={this.state.loading}
            />
        );
    }
}
export default CpuTopTable;

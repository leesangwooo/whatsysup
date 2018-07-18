import * as React from 'react';
export interface ITimelineChartProps {
    data: Array<{
        x: string;
        y1: string;
        y2: string;
        y3: string;
        y4?: string;
        y5?: string;
    }>;
    titleMap: { y1: string; y2: string; y3: string; y4: string; y5: string };
    padding?: [number, number, number, number];
    height?: number;
    style?: React.CSSProperties;
    callbackFn?: any;
    cpuTop: {};
    cpuHeaders: {};
}

export default class TimelineChart extends React.Component<ITimelineChartProps, any> {}

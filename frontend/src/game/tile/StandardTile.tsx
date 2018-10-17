import * as React from 'react';
import { Rect } from 'react-konva';
import { ICoordinate } from '../game.typings';

interface IProps {
    key: number,
    colour: string
    coordinate: ICoordinate,
    width: number,
    height: number
}

export default class ColouredTile extends React.Component<IProps, any> {

    public render() {
        return(
            <Rect
                x={this.props.coordinate.x}
                y={this.props.coordinate.y}
                width={this.props.width}
                height={this.props.width}
                fill = {this.props.colour}
                cornerRadius = {5}
                stroke={'black'}
            />
        );
    }
}
import * as React from 'react';
import { Rect } from 'react-konva';
import { ICoordinate } from '../game.typings';

interface IProps {
    coordinate: ICoordinate,
    width: number,
    height: number
}

const EmptyTile = (props: IProps) => {
    return(
        <Rect
            x={props.coordinate.x}
            y={props.coordinate.y}
            width={props.width}
            height={props.width}
            fill = {'#f2f5f9'}
            cornerRadius = {3}
            stroke = {'black'}
        />
    );
}

export default EmptyTile;
import * as React from 'react';
import { Image as KonvaImage }  from 'react-konva';
import { ICoordinate } from '../game.typings';

interface IProps {
    coordinate: ICoordinate,
    width: number,
    height: number
}

const BombTile = (props: IProps) => {
    const image  = new Image();
    image.src = 'resources/bomb.png';
    return(
        <KonvaImage
            image={image}
            x={props.coordinate.x}
            y={props.coordinate.y}
            width={props.width}
            height={props.width}
        />
    );
}

export default BombTile;
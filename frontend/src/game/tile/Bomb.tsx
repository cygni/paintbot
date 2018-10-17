import * as React from 'react';
import { Image as KonvaImage }  from 'react-konva';
import { IBomb } from '../game.typings';

interface IProps {
    bomb: IBomb
    width: number,
    height: number
}

const Bomb = (props: IProps) => {
    const image  = new Image();
    image.src = props.bomb.image;
    return(
        <KonvaImage
            image={image}
            x={props.bomb.coordinate.x}
            y={props.bomb.coordinate.y}
            width={props.width}
            height={props.width}
        />
    );
}

export default Bomb;
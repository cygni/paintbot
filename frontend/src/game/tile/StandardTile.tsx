import * as Konva from 'konva';
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

    public tile: Konva.Rect;

    public shouldComponentUpdate(nextProps: IProps) {
        return( 
            nextProps.colour !== this.props.colour ||
            nextProps.coordinate.x !== this.props.coordinate.x ||
            nextProps.coordinate.y !== this.props.coordinate.y
        );          
    }

    public componentDidMount() {
        this.animate();
    }

    public componentDidUpdate() {
        this.animate();
    }

    public animate() {
        const tween = new Konva.Tween({
            node: this.tile,
            opacity: 1,
            duration: 0.5,
            easing: Konva.Easings.EaseInOut
        });

        tween.play();
    }

    public render() {
        return(
            <Rect
                x={this.props.coordinate.x}
                y={this.props.coordinate.y}
                fill={this.props.colour}
                opacity={0}
                width={this.props.width}
                height={this.props.width}
                cornerRadius = {5}
                stroke={'black'}
                ref={ (node: Konva.Rect) => { 
                    if(node !== null) {
                        this.tile = node;
                }  } }
            />
        );
    }
}
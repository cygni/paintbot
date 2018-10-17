import * as Konva from 'konva';
import * as React from 'react';
import { Circle, Group } from 'react-konva';
import { ICoordinate } from '../game.typings';

interface IProps {
    key: number,
    colour: string
    coordinate: ICoordinate
    width: number,
    height: number,
    playerId: string;
    previousCoordinate: ICoordinate
}

export default class Character extends React.Component<IProps> {

    public character: Konva.Group;

    public componentDidUpdate() {
        this.animate();
    }

    public shouldComponentUpdate(nextProps: IProps) {
        return this.props.coordinate.x !== nextProps.coordinate.x || this.props.coordinate.y !== nextProps.coordinate.y;
    }

    public animate() {
        const tween = new Konva.Tween({
            node: this.character,
            x: this.props.coordinate.x,
            y: this.props.coordinate.y,
            duration: 0.5,
            easing: Konva.Easings.EaseInOut
        });

        tween.play();
    }

    public render() {
        return (
            <Group x={this.props.previousCoordinate.x} y={this.props.previousCoordinate.y} width={this.props.width} height={this.props.height}
                ref={ (node: Konva.Group) => { 
                    if(node !== null) {
                        this.character = node;
                }  } }
            >
                <Circle offsetX={-this.props.width/2} offsetY={-this.props.height/2} radius={this.props.width/2} fill={this.props.colour} stroke={'#001638'}/>
                <Circle offsetX={-this.props.width/2} offsetY={-this.props.height/2} radius={this.props.width/4} fill={this.props.colour} stroke={'#001638'}/>
            </Group>
        );
    }
}


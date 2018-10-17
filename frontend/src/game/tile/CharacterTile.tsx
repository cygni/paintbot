import * as Konva from 'konva';
import * as React from 'react';
import { Circle, Group } from 'react-konva';
import { ICoordinate } from '../game.typings';
import './Tile.css';

interface IProps {
    colour: string
    coordinate: ICoordinate
    width: number,
    height: number,
    playerId: string;
}

interface IState {
    coordinate: ICoordinate
}

export default class CharacterTile extends React.Component<IProps, IState> {

    public character: Konva.Group;

    public constructor(props: IProps) {
        super(props);
        this.state = {
            coordinate: { x: 0, y: 0 }
        }
    }

    public componentDidMount() {
        this.character.to({
            x: this.props.coordinate.x,
            y: this.props.coordinate.y,
            duration: 0.8
        });
        sessionStorage.removeItem(this.props.playerId);
        sessionStorage.setItem(this.props.playerId, JSON.stringify(this.props.coordinate));
        this.setState({ coordinate: this.props.coordinate });
    }

    public render() {
        const lastPosition = JSON.parse(sessionStorage.getItem(this.props.playerId) || '{}');
        const coordinate = Object.keys(lastPosition).length > 0 ? lastPosition: this.props.coordinate;
        return (
            <Group x={coordinate.x} y={coordinate.y} width={this.props.width} height={this.props.height}
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


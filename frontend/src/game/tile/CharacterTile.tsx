import * as Konva from 'konva';
import * as React from 'react';
import { Circle, Group } from 'react-konva';
import { Coordinate } from '../type';

interface Props {
  key: number;
  colour: string;
  coordinate: Coordinate;
  width: number;
  height: number;
  playerId: string;
  previousCoordinate: Coordinate;
}

export default class CharacterTile extends React.Component<Props> {
  public character: Konva.Group;

  public componentDidMount() {
    this.character.transformsEnabled('position');
  }

  public componentWillUnmount() {
    this.character.destroy();
  }

  public componentDidUpdate() {
    this.animate();
    this.character.cache();
  }

  public shouldComponentUpdate(nextProps: Props) {
    return (
      this.props.coordinate.x !== nextProps.coordinate.x ||
      this.props.coordinate.y !== nextProps.coordinate.y
    );
  }

  public animate() {
    this.character.to({
      x: this.props.coordinate.x,
      y: this.props.coordinate.y,
      duration: 0.5,
      easing: Konva.Easings.EaseInOut,
    });
  }

  public render() {
    return (
      <Group
        x={this.props.previousCoordinate.x}
        y={this.props.previousCoordinate.y}
        width={this.props.width}
        height={this.props.height}
        listening={false}
        ref={(node: Konva.Group) => {
          if (node !== null) {
            this.character = node;
          }
        }}
      >
        <Circle perfectDrawEnabled={false} offsetX={-this.props.width / 2} offsetY={-this.props.height / 2} radius={this.props.width / 2} fill={this.props.colour} stroke={'#001638'} />
        <Circle perfectDrawEnabled={false} offsetX={-this.props.width / 2} offsetY={-this.props.height / 2} radius={this.props.width / 4} fill={this.props.colour} stroke={'#001638'} />
        <Circle perfectDrawEnabled={false} offsetX={-this.props.width / 2} offsetY={-this.props.height / 2} radius={this.props.width / 16} fill={'#001638'} stroke={'#001638'} />
      </Group>
    );
  }
}

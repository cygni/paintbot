import Konva from 'konva';
import React from 'react';
import { Circle, Group } from 'react-konva';

import { CharacterColors } from '../../../common/Constants';
import { Coordinate } from '../../type';

interface Props {
  key: number;
  colour: string;
  coordinate: Coordinate;
  width: number;
  height: number;
  playerId: string;
  previousCoordinate: Coordinate;
}

export default class PlayerCharacter extends React.Component<Props> {
  private readonly characterRef = React.createRef<Konva.Group>();

  componentDidMount() {
    this.characterRef.current!.transformsEnabled('position');
  }

  componentWillUnmount() {
    this.characterRef.current!.destroy();
  }

  componentDidUpdate() {
    this.animate();
    this.characterRef.current!.cache();
  }

  shouldComponentUpdate(nextProps: Props) {
    return this.props.coordinate.x !== nextProps.coordinate.x || this.props.coordinate.y !== nextProps.coordinate.y;
  }

  animate() {
    this.characterRef.current!.to({
      x: this.props.coordinate.x,
      y: this.props.coordinate.y,
      duration: 0.25,
      easing: Konva.Easings.EaseInOut,
    });
  }

  render() {
    return (
      <Group
        x={this.props.previousCoordinate.x}
        y={this.props.previousCoordinate.y}
        width={this.props.width}
        height={this.props.height}
        listening={false}
        ref={this.characterRef}
      >
        <Circle
          perfectDrawEnabled={false}
          offsetX={-this.props.width / 2}
          offsetY={-this.props.height / 2}
          radius={this.props.width / 2}
          fill={this.props.colour}
          stroke={CharacterColors.Stroke}
        />
        <Circle
          perfectDrawEnabled={false}
          offsetX={-this.props.width / 2}
          offsetY={-this.props.height / 2}
          radius={this.props.width / 4}
          fill={this.props.colour}
          stroke={CharacterColors.Stroke}
        />
        <Circle
          perfectDrawEnabled={false}
          offsetX={-this.props.width / 2}
          offsetY={-this.props.height / 2}
          radius={this.props.width / 16}
          fill={CharacterColors.Eye}
          stroke={CharacterColors.Stroke}
        />
      </Group>
    );
  }
}

import Konva from 'konva';
import React from 'react';
import { Rect } from 'react-konva';

import { TileColors } from '../../../common/Constants';
import { Coordinate } from '../../type';

interface Props {
  key: number;
  colour: string;
  coordinate: Coordinate;
  width: number;
  height: number;
}

export default class ColouredTile extends React.Component<Props> {
  private readonly tileRef = React.createRef<Konva.Rect>();

  shouldComponentUpdate(nextProps: Props) {
    return (
      nextProps.colour !== this.props.colour ||
      nextProps.coordinate.x !== this.props.coordinate.x ||
      nextProps.coordinate.y !== this.props.coordinate.y
    );
  }

  componentDidMount() {
    this.animate();
    this.tileRef.current!.cache();
    this.tileRef.current!.transformsEnabled('position');
  }

  componentWillUnmount() {
    this.tileRef.current!.destroy();
  }

  componentDidUpdate() {
    this.animate();
    this.tileRef.current!.cache();
  }

  animate() {
    this.tileRef.current!.to({
      opacity: 1,
      duration: 0.5,
      easing: Konva.Easings.StrongEaseIn,
    });
  }

  render() {
    return (
      <Rect
        x={this.props.coordinate.x}
        y={this.props.coordinate.y}
        fill={this.props.colour}
        opacity={0}
        width={this.props.width}
        height={this.props.width}
        cornerRadius={5}
        stroke={TileColors.Stroke}
        perfectDrawEnabled={false}
        listening={false}
        ref={this.tileRef}
      />
    );
  }
}

import * as Konva from 'konva';
import * as React from 'react';
import { Rect } from 'react-konva';
import { TileColors } from '../../common/Constants';
import { Coordinate } from '../type';

interface Props {
  key: number;
  colour: string;
  coordinate: Coordinate;
  width: number;
  height: number;
}

export default class ColouredTile extends React.Component<Props> {
  public tile: Konva.Rect;

  public shouldComponentUpdate(nextProps: Props) {
    return (
      nextProps.colour !== this.props.colour ||
      nextProps.coordinate.x !== this.props.coordinate.x ||
      nextProps.coordinate.y !== this.props.coordinate.y
    );
  }

  public componentDidMount() {
    this.animate();
    this.tile.cache();
    this.tile.transformsEnabled('position');
  }

  public componentWillUnmount() {
    this.tile.destroy();
  }

  public componentDidUpdate() {
    this.animate();
    this.tile.cache();
  }

  public animate() {
    this.tile.to({
      opacity: 1,
      duration: 0.5,
      easing: Konva.Easings.EaseInOut,
    });
  }

  public render() {
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
        ref={(node: Konva.Rect) => {
          if (node !== null) {
            this.tile = node;
          }
        }}
      />
    );
  }
}

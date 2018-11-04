import { Vector2d } from 'konva';
import * as Konva from 'konva';
import * as React from 'react';
import { Path } from 'react-konva';

import { Coordinate } from '../../type';

interface Props {
  key: number;
  colour: string;
  coordinate: Coordinate;
  width: number;
  height: number;
}

export default class ColouredTile extends React.Component<Props> {
  private readonly pathRef = React.createRef<Konva.Path>();
  private readonly svgTileWidth = 63;
  private readonly svgTileHeight = 63;
  private readonly svgData =
    `M64,56c0-6,8-4,8-11s-8-2-8-9s8-1.73,8-8s-8-6-8-12s3-9,3-9l-1.1-1.1C63.84,3.92,59.28,0,55,0
		c-5.5,0-5.5,8-11,8c-5.87,0-3-8-8-8s-1.55,8-7.5,8S21,0,16,0S7,3,7,3c0.5,0.5,1.5,2.5,0,4S3.5,7.5,3,7c0,0-3,3-3,9s8,5.73,8,12
		s-8,1-8,8s8,2,8,9s-8,5-8,11s6.5,10.5,7,11c0.07-0.07,0.19-0.15,0.36-0.25C8.43,66.04,11.87,64,16,64c5,0,6.55,8,12.5,8
		s2.5-8,7.5-8s2.13,8,8,8c5.5,0,5.5-8,11-8c0.31,0,0.63,0.03,0.94,0.07c0.09,0.01,0.19,0.03,0.28,0.05
		c0.23,0.04,0.46,0.08,0.69,0.14c0.1,0.02,0.2,0.05,0.29,0.07c0.26,0.07,0.51,0.15,0.76,0.24c0.06,0.02,0.12,0.04,0.17,0.06
		c0.32,0.12,0.63,0.25,0.94,0.39c0.05,0.02,0.11,0.05,0.16,0.08c0.26,0.12,0.51,0.25,0.76,0.38c0.08,0.04,0.16,0.09,0.24,0.13
		c0.23,0.13,0.47,0.26,0.69,0.4c0.07,0.04,0.13,0.08,0.2,0.12c0.58,0.36,1.13,0.74,1.65,1.13c0.08,0.06,0.15,0.11,0.22,0.17
		c0.18,0.14,0.36,0.27,0.53,0.41c0.08,0.07,0.17,0.13,0.25,0.2c0.16,0.13,0.31,0.25,0.46,0.38c0.08,0.06,0.15,0.13,0.23,0.19
		c0.19,0.16,0.37,0.32,0.54,0.48c0.13,0.12,0.25,0.22,0.36,0.33c0.04,0.04,0.09,0.08,0.13,0.12c0.8,0.75,1.34,1.32,1.47,1.45
		c0.5,0.5,2.5,1.5,4,0s0.5-3.5,0-4S64,62,64,56z`;

  shouldComponentUpdate(nextProps: Props) {
    return (
      nextProps.colour !== this.props.colour ||
      nextProps.coordinate.x !== this.props.coordinate.x ||
      nextProps.coordinate.y !== this.props.coordinate.y
    );
  }

  componentDidMount() {
    this.animate();
    this.pathRef.current!.cache();
  }

  componentWillUnmount() {
    this.pathRef.current!.destroy();
  }

  componentDidUpdate() {
    this.animate();
    this.pathRef.current!.cache();
  }

  animate() {
    this.pathRef.current!.to({
      opacity: 1,
      duration: 0.5,
      easing: Konva.Easings.StrongEaseIn,
    });
  }

  render() {
    const { colour, width, height } = this.props;
    const tileScaleX = width / this.svgTileWidth;
    const tileScaleY = height / this.svgTileHeight;
    const { x, y } = this.props.coordinate;
    const scale: Vector2d = {
      x: tileScaleX,
      y: tileScaleY,
    };
    return (
      <Path
        data={this.svgData}
        fill={colour}
        x={x}
        y={y}
        scale={scale}
        opacity={0}
        perfectDrawEnabled={false}
        listening={false}
        ref={this.pathRef}
      />
    );
  }
}

import Konva from 'konva';
import React from 'react';
import { Image as KonvaImage } from 'react-konva';

import { PowerUp } from '../../type';

interface Props {
  bomb: PowerUp;
  width: number;
  height: number;
}

export default class Bomb extends React.Component<Props> {
  private readonly image = new Image();
  private readonly bombRef = React.createRef<Konva.Image>();

  shouldComponentUpdate(nextProps: Props) {
    return (
      nextProps.bomb.coordinate.x !== this.props.bomb.coordinate.x ||
      nextProps.bomb.coordinate.y !== this.props.bomb.coordinate.y
    );
  }

  componentDidMount() {
    this.image.src = this.props.bomb.image;
  }

  componentWillUnmount() {
    this.bombRef.current!.destroy();
  }

  render() {
    const { bomb, width, height } = this.props;
    return (
      <KonvaImage
        image={this.image}
        x={bomb.coordinate.x}
        y={bomb.coordinate.y}
        width={width}
        height={height}
        perfectDrawEnabled={false}
        listening={false}
        ref={this.bombRef}
      />
    );
  }
}

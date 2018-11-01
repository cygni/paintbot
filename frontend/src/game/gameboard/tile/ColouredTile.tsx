import {Vector2d} from "konva";
import * as Konva from 'konva';
import * as React from 'react';
import {Path} from 'react-konva';
import {Coordinate} from '../../type';

interface Props {
    key: number;
    colour: string;
    coordinate: Coordinate;
    width: number;
    height: number;
}

export default class ColouredTile extends React.Component<Props> {

    private tile: Konva.Path;
    private svgTileWidth = 63;
    private svgTileHeight = 63;
    private svgData = "M8 68c0 0 3-4 8-4s6.5 8 12.5 8s2.5-8 7.5-8s2.1 8,8,8c5.5,0,5.5-8,11-8s8.5,3.5,9,4s2.5,1.5,4,0s0.5-3.5,0-4s-4-2-4-8s8-4,8-11s-8-2-8-9s8-1.7,8-8s-8-6-8-12s4-8,4-8l-4-4c0,0-3.5-4-9-4s-5.5,8-11,8c-5.9,0-3-8-8-8s-1.5,8-7.5,8S21,0,16,0S7.5,3.5,8,4s1.5,2.5,0,4S4.5,8.5,4,8s-4,2-4,8s8,5.7,8,12s-8,1-8,8s8,2,8,9s-8,5-8,11s4,8,4,8L8,68z";

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
            easing: Konva.Easings.StrongEaseIn,
        });
    }

    public render() {
        const {colour, width, height} = this.props;
        const tileScaleX = width / this.svgTileWidth;
        const tileScaleY = height / this.svgTileHeight;
        const {x, y} = this.props.coordinate;
        const scale: Vector2d = {
            x: tileScaleX,
            y: tileScaleY
        };
        return (
            <Path data={this.svgData}
                  fill={colour}
                  x={x}
                  y={y}
                  scale={scale}
                  opacity={0}
                  perfectDrawEnabled={false}
                  listening={false}
                  ref={(node: Konva.Path) => {
                      if (node !== null) {
                          this.tile = node;
                      }
                  }}
            />);
    }
}
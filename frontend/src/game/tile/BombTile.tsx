import * as Konva from 'konva';
import * as React from 'react';
import { Image as KonvaImage } from 'react-konva';
import { Bomb } from '../type';

interface Props {
    bomb: Bomb;
    width: number;
    height: number;
}

export default class BombTile extends React.Component<Props> {

    public image: HTMLImageElement;
    public bomb: Konva.Image;

    public constructor(props: Props) {
        super(props);
        this.image = new Image();
    }

    public shouldComponentUpdate(nextProps: Props) {
        return (
            nextProps.bomb.coordinate.x !== this.props.bomb.coordinate.x ||
            nextProps.bomb.coordinate.y !== this.props.bomb.coordinate.y
        );
    }

    public componentDidMount() {
        this.image.src = this.props.bomb.image;
    }

    public componentWillUnmount() {
        this.bomb.destroy();
    }

    public render() {
        return (
            <KonvaImage
                image={this.image}
                x={this.props.bomb.coordinate.x}
                y={this.props.bomb.coordinate.y}
                width={this.props.width}
                height={this.props.width}
                perfectDrawEnabled={false}
                listening={false}
                ref={
                    (node: Konva.Image) => {
                        if (node !== null) {
                            this.bomb = node;
                        }
                    }
                }

            />
        );
    }
}

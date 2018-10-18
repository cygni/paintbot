import * as React from 'react';
import { Image as KonvaImage }  from 'react-konva';
import { IBomb } from '../game.typings';

interface IProps {
    bomb: IBomb
    width: number,
    height: number
}

export default class Bomb extends React.Component<IProps> {
    public image: HTMLImageElement;

    public constructor(props: IProps) {
        super(props);
        this.image = new Image();
    }

    public shouldComponentUpdate(nextProps: IProps) {
        return nextProps.bomb.coordinate.x !== this.props.bomb.coordinate.x 
            || nextProps.bomb.coordinate.y !== this.props.bomb.coordinate.y 
    }

    public componentDidMount() {
        this.image.src = this.props.bomb.image;
    }

    public render() {
        return(
            <KonvaImage
                image={this.image}
                x={this.props.bomb.coordinate.x}
                y={this.props.bomb.coordinate.y}
                width={this.props.width}
                height={this.props.width}
            />
        );
    }
}

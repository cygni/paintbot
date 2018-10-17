import * as React from 'react';
import { Layer, Stage } from 'react-konva';
import { ICoordinate, ITile} from '../game.typings';
import Tile from '../tile/Tile';

interface IProps {
    tiles: Map<string, ITile>,
    width: number,
    height: number;
    tileWidth: number,
    tileHeight: number
}

export default class GameBoardContainer extends React.Component<IProps> {
    public BOARD_WIDTH: number;
    public BOARD_HEIGHT: number;

    constructor(props: IProps) {
        super(props);
        this.BOARD_WIDTH = this.props.width * this.props.tileWidth;
        this.BOARD_HEIGHT = this.props.height * this.props.tileHeight;
    }

    public render() {
        return (
            <Stage width={this.BOARD_WIDTH} height={this.BOARD_HEIGHT}>
                <Layer>
                    {this.getTileComponents()}
                </Layer>
            </Stage>
        );
    }

    public getTileComponents() {
        const tiles = Array.from(this.props.tiles.values());
        return tiles.map((tile, index) =>{
            tile.coordinate = this.getBoardCoordinate(tile.coordinate);
            return <Tile key={index} tile={tile} width={this.props.tileWidth} height={this.props.tileHeight} playerId={tile.playerId} />
        });
    }

    private getBoardCoordinate(coordinate: ICoordinate): ICoordinate {
        const boardCoordinate = {} as ICoordinate;
        boardCoordinate.x = coordinate.x * this.props.tileWidth;
        boardCoordinate.y = coordinate.y * this.props.tileHeight;

        return boardCoordinate;
    }
}

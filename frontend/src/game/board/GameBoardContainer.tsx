import * as React from 'react';
import {  ICoordinate, ITile} from '../game.typings';

const TILE_WIDTH = 40;
const TILE_HEIGHT = 40;

interface IProps {
    tiles: Map<ICoordinate, ITile>,
    width: number,
    height: number;
}

interface IState {
    tiles: Map<ICoordinate, ITile>,
    width: number,
    height: number
}

export default class GameBoardContainer extends React.Component<IProps, IState> {
    public canvas: any;
    public BOARD_WIDTH: number;
    public BOARD_HEIGHT: number;
    constructor(props: IProps) {
        super(props);
        this.canvas = React.createRef();
        this.state = {
            height: this.props.height,
            tiles: this.props.tiles,
            width: this.props.width,
        }

        this.BOARD_WIDTH = this.state.width * TILE_WIDTH;
        this.BOARD_HEIGHT = this.state.height * TILE_HEIGHT;
    }
    

    public componentDidMount () {
        if(this.state && this.state.tiles) {
            const canvas = this.canvas.current as HTMLCanvasElement;
            const ctx = canvas.getContext('2d') as CanvasRenderingContext2D;
            const tilesArray = Array.from(this.state.tiles.values());
            for(let i = 0; i < this.BOARD_WIDTH - TILE_WIDTH; i += TILE_WIDTH) {
                for(let j = 0; j < this.BOARD_HEIGHT - TILE_HEIGHT; j += TILE_HEIGHT) {
                    ctx.rect(i, j, TILE_WIDTH, TILE_HEIGHT);
                }
            }
            for (const tile of tilesArray) {
                ctx.fillStyle = tile.colorOfInhabitingPlayer || tile.color || '';
                ctx.fillRect(this.getRealX(tile.coordinate.x), this.getRealY(tile.coordinate.y) , TILE_WIDTH, TILE_HEIGHT);
                ctx.stroke();
            }
        }
    }

    public render() {
        return (
            <div>
                <canvas ref={this.canvas} width={this.BOARD_WIDTH} height={this.BOARD_HEIGHT} />
            </div>
        )
    }

    private getRealX(gameX: number) {
        return gameX * TILE_WIDTH;
    }

    private getRealY(gameY: number) {
        return gameY * TILE_HEIGHT;
    }
}

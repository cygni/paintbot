import * as React from 'react';
import { getBlobSvgString, getObstacleSvgString } from '../game-components/Blob';
import { ICoordinate, ITile, TileType } from '../game.typings';

interface IProps {
    tiles: Map<ICoordinate, ITile>,
    width: number,
    height: number;
    tileWidth: number,
    tileHeight: number
}

interface IState {
    tiles: Map<ICoordinate, ITile>,
}

export default class GameBoardContainer extends React.Component<IProps, IState> {
    public canvas: any;
    public BOARD_WIDTH: number;
    public BOARD_HEIGHT: number;

    constructor(props: IProps) {
        super(props);
        this.canvas = React.createRef();
        this.state = {
            tiles: this.props.tiles,
        }

        this.BOARD_WIDTH = this.props.width * this.props.tileWidth;
        this.BOARD_HEIGHT = this.props.height * this.props.tileHeight;
    }

    public componentDidMount () {
        if(this.state && this.state.tiles) {
            const ctx = this.initializeCanvas();
            this.drawTiles(ctx);
        }
    }

    public render() {
        return (
            <div>
                <canvas ref={this.canvas} width={this.BOARD_WIDTH} height={this.BOARD_HEIGHT} />
            </div>
        )
    }

    private initializeCanvas(): CanvasRenderingContext2D {
        const canvas = this.canvas.current as HTMLCanvasElement;
        const ctx = canvas.getContext('2d') as CanvasRenderingContext2D;
        ctx.strokeStyle = 'white';
        ctx.lineWidth = 1;
        return ctx;
    }

    private drawTiles(ctx:CanvasRenderingContext2D) {
        const tiles = Array.from(this.state.tiles.values());
        for (const tile of tiles) {
            if (tile.type === TileType.EMPTY) {
                if (tile.isColoured) {
                    ctx.fillStyle = tile.colour;
                    ctx.fillRect(this.getRealX(tile.coordinate.x), this.getRealY(tile.coordinate.y) , this.props.tileWidth - 1, this.props.tileHeight - 1);
                }else {
                    this.roundRect(ctx, this.getRealX(tile.coordinate.x), this.getRealY(tile.coordinate.y) , this.props.tileWidth, this.props.tileHeight, 5);
                }
            } else if(tile.type === TileType.CHARACTER) {
                const svgPath = 'data:image/svg+xml,' + escape(getBlobSvgString(this.props.tileWidth- 1, this.props.tileHeight - 1, tile.colourOfInhabitingPlayer));
                this.addImageToCanvasFromSvgString(ctx, tile.coordinate, svgPath);
            } else if(tile.type === TileType.OBSTACLE) {
                const svgPath = 'data:image/svg+xml,' + escape(getObstacleSvgString(this.props.tileWidth, this.props.tileHeight));
                this.addImageToCanvasFromSvgString(ctx, tile.coordinate, svgPath);
            }
            ctx.stroke();
        }
    } 

    private getRealX(gameX: number) {
        return gameX * this.props.tileWidth;
    }

    private getRealY(gameY: number) {
        return gameY * this.props.tileHeight;
    }

    private addImageToCanvasFromSvgString(ctx: CanvasRenderingContext2D, coordinate: ICoordinate, source: string) {
        ctx.save();
        const image = new Image();
        image.onload = () => {
            ctx.drawImage(image, this.getRealX(coordinate.x), this.getRealY(coordinate.y));
        }
             
        image.src = source;

        ctx.restore();
    }

    private roundRect (ctx: CanvasRenderingContext2D, x: number, y: number, w: number, h: number, r: number) {
        if (w < 2 * r) { 
            r = w / 2;
        }
        if (h < 2 * r) {
            r = h / 2;
        }
        ctx.beginPath();
        ctx.moveTo(x+r, y);
        ctx.arcTo(x+w, y,   x+w, y+h, r);
        ctx.arcTo(x+w, y+h, x,   y+h, r);
        ctx.arcTo(x,   y+h, x,   y,   r);
        ctx.arcTo(x,   y,   x+w, y,   r);
        ctx.closePath();
        return ctx;
      }
}

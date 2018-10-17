import * as React from 'react';
import { Layer, Stage } from 'react-konva';
import { IBomb, ICharacter, ICoordinate, ITile} from '../game.typings';
import Bomb from '../tile/Bomb';
import Character from '../tile/Character';
import TileContainer from '../tile/TileContainer';

interface IProps {
    tiles: Map<string, ITile>,
    characters: Map<string, ICharacter>
    previousCharacters: Map<string, ICharacter>
    bombs: IBomb[]
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
                    {this.getCharacterComponents()}
                    {this.getBombComponents()}
                </Layer>
            </Stage>
        );
    }

    public getTileComponents() {
        const tiles = Array.from(this.props.tiles.values());
        return tiles.map((tile, index) =>{
            tile.coordinate = this.getBoardCoordinate(tile.coordinate);
            return <TileContainer key={index} tile={tile} width={this.props.tileWidth} height={this.props.tileHeight}/>
        });
    }

    public getCharacterComponents() {
        const characters = Array.from(this.props.characters.values());
        return characters.map((character, index) => {
            character.coordinate = this.getBoardCoordinate(character.coordinate);
            const previousCharacter = this.props.previousCharacters.get(character.id);
            const previousCharacterCoordinate = previousCharacter ? previousCharacter.coordinate: character.coordinate;
            return <Character key={index} colour={character.colour} coordinate={character.coordinate} width={this.props.tileWidth} height={this.props.tileHeight} playerId={character.id} previousCoordinate={previousCharacterCoordinate} />
        });
    }

    public getBombComponents() {
        return this.props.bombs.map((bomb, index) => {
            bomb.coordinate = this.getBoardCoordinate(bomb.coordinate);
            return <Bomb key={index} bomb={bomb} width={this.props.tileWidth} height={this.props.tileHeight}/>
        });
    }

    private getBoardCoordinate(coordinate: ICoordinate): ICoordinate {
        const boardCoordinate = {} as ICoordinate;
        boardCoordinate.x = coordinate.x * this.props.tileWidth;
        boardCoordinate.y = coordinate.y * this.props.tileHeight;

        return boardCoordinate;
    }
}

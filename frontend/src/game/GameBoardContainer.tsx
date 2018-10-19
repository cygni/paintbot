import * as Konva from 'konva';
import * as React from 'react';
import { Layer, Stage } from 'react-konva';
import BombTile from './tile/BombTile';
import CharacterTile from './tile/CharacterTile';
import StandardTile from './tile/StandardTile';
import { Bomb, Character, Coordinate, Tile } from './type';

interface Props {
  tiles: Map<string, Tile>;
  characters: Map<string, Character>;
  previousCharacters: Map<string, Character>;
  bombs: Bomb[];
  width: number;
  height: number;
  tileWidth: number;
  tileHeight: number;
}

export default class GameBoardContainer extends React.Component<Props> {
  public BOARD_WIDTH: number;
  public BOARD_HEIGHT: number;
  public stageRef: Konva.Stage

  constructor(props: Props) {
    super(props);
    this.BOARD_WIDTH = this.props.width * this.props.tileWidth;
    this.BOARD_HEIGHT = this.props.height * this.props.tileHeight;
  }

  public componentWillUnmount() {
    this.stageRef.getStage().destroyChildren();
    this.stageRef.getStage().destroy();
    this.stageRef.destroyChildren();
    this.stageRef.destroy();
  }

  public render() {
    return (
      <Stage
        className={'stage'}
        width={this.BOARD_WIDTH}
        height={this.BOARD_HEIGHT}
        listening={false}
        ref={(stage: any) => {
          if (stage !== null) {
            this.stageRef = stage
          }
        }}
      >
        <Layer hitGraphEnabled={false} listening={false}>
          {this.getTileComponents()}
          {this.getCharacterComponents()}
          {this.getBombComponents()}
        </Layer>
      </Stage>
    );
  }

  public getTileComponents() {
    const tiles = Array.from(this.props.tiles.values());
    return tiles.map((tile, index) => {
      tile.coordinate = this.getBoardCoordinate(tile.coordinate);
      return (
        <StandardTile
          key={index}
          coordinate={tile.coordinate}
          colour={tile.colour}
          width={this.props.tileWidth}
          height={this.props.tileHeight}
        />
      );
    });
  }

  public getCharacterComponents() {
    const characters = Array.from(this.props.characters.values());
    return characters.map((character, index) => {
      character.coordinate = this.getBoardCoordinate(character.coordinate);
      const previousCharacter = this.props.previousCharacters.get(character.id);
      const previousCharacterCoordinate = previousCharacter
        ? previousCharacter.coordinate
        : character.coordinate;
      return (
        <CharacterTile
          key={index}
          colour={character.colour}
          coordinate={character.coordinate}
          width={this.props.tileWidth}
          height={this.props.tileHeight}
          playerId={character.id}
          previousCoordinate={previousCharacterCoordinate}
        />
      );
    });
  }

  public getBombComponents() {
    return this.props.bombs.map((bomb, index) => {
      bomb.coordinate = this.getBoardCoordinate(bomb.coordinate);
      return (
        <BombTile
          key={index}
          bomb={bomb}
          width={this.props.tileWidth}
          height={this.props.tileHeight}
        />
      );
    });
  }

  private getBoardCoordinate(coordinate: Coordinate): Coordinate {
    const boardCoordinate = {} as Coordinate;
    boardCoordinate.x = coordinate.x * this.props.tileWidth;
    boardCoordinate.y = coordinate.y * this.props.tileHeight;

    return boardCoordinate;
  }
}

import * as Konva from 'konva';
import * as React from 'react';
import { Layer, Stage } from 'react-konva';
import BombTile from './tile/BombTile';
import CharacterTile from './tile/CharacterTile';
import StandardTile from './tile/StandardTile';
import { Bomb, Character, Coordinate, Tile } from './type';

interface Props {
  tiles: Map<string, Tile>;
  characters: Character[];
  previousCharacters: Character[];
  bombs: Bomb[];
  width: number;
  height: number;
  tileWidth: number;
  tileHeight: number;
}

export default class GameBoardContainer extends React.Component<Props> {
  private readonly boardWidth: number;
  private readonly boardHeight: number;
  private stageRef: Konva.Stage;

  constructor(props: Props) {
    super(props);
    this.boardWidth = this.props.width * this.props.tileWidth;
    this.boardHeight = this.props.height * this.props.tileHeight;
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
        width={this.boardWidth}
        height={this.boardHeight}
        listening={false}
        ref={(stage: any) => {
          if (stage !== null) {
            this.stageRef = stage;
          }
        }}
      >
        <Layer hitGraphEnabled={false} listening={false}>
          {this.renderTileComponents()}
          {this.renderCharacterComponents()}
          {this.renderBombComponents()}
        </Layer>
      </Stage>
    );
  }

  public renderTileComponents() {
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

  public renderCharacterComponents() {
    const {
      characters,
      previousCharacters,
      tileWidth,
      tileHeight,
    } = this.props;
    return characters.map((character, index) => {
      character.coordinate = this.getBoardCoordinate(character.coordinate);
      const previousCharacter = previousCharacters.filter(
        c => c.id === character.id,
      )[0];
      const previousCharacterCoordinate = previousCharacter
        ? previousCharacter.coordinate
        : character.coordinate;
      return (
        <CharacterTile
          key={index}
          colour={character.colour}
          coordinate={character.coordinate}
          width={tileWidth}
          height={tileHeight}
          playerId={character.id}
          previousCoordinate={previousCharacterCoordinate}
        />
      );
    });
  }

  public renderBombComponents() {
    const { bombs, tileWidth, tileHeight } = this.props;
    return bombs.map((bomb, index) => {
      bomb.coordinate = this.getBoardCoordinate(bomb.coordinate);
      return (
        <BombTile
          key={index}
          bomb={bomb}
          width={tileWidth}
          height={tileHeight}
        />
      );
    });
  }

  private getBoardCoordinate(coordinate: Coordinate): Coordinate {
    const { tileWidth, tileHeight } = this.props;
    const boardCoordinate: Coordinate = { x: 0, y: 0 };
    boardCoordinate.x = coordinate.x * tileWidth;
    boardCoordinate.y = coordinate.y * tileHeight;
    return boardCoordinate;
  }
}

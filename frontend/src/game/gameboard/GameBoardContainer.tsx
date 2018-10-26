import * as React from 'react';
import { Layer, Stage } from 'react-konva';
import styled from 'styled-components';
import { GameBoardConstants } from '../../common/Constants';
import { Coordinate, Game } from '../type';
import Bomb from './gameobject/Bomb';
import PlayerCharacter from './gameobject/PlayerCharacter';
import StandardTile from './tile/StandardTile';

interface Props {
  game: Game;
}

const Container = styled.div`
  border: ${GameBoardConstants.Border};
`;

export default class GameBoardContainer extends React.Component<Props> {
  private readonly boardWidth: number;
  private readonly boardHeight: number;

  constructor(props: Props) {
    super(props);
    const { width, height } = this.props.game;
    this.boardWidth = width * this.calculateTileSize(width);
    this.boardHeight = height * this.calculateTileSize(width);
  }

  public render() {
    return (
      <Container>
        <Stage
          className={'stage'}
          width={this.boardWidth}
          height={this.boardHeight}
          listening={false}
        >
          <Layer hitGraphEnabled={false} listening={false}>
            {this.renderTileComponents()}
            {this.renderCharacterComponents()}
            {this.renderBombComponents()}
          </Layer>
        </Stage>
      </Container>
    );
  }

  public renderTileComponents() {
    const { width } = this.props.game;
    const tiles = Array.from(this.props.game.tiles.values());
    return tiles.map((tile, index) => {
      tile.coordinate = this.getBoardCoordinate(tile.coordinate);
      return (
        <StandardTile
          key={index}
          coordinate={tile.coordinate}
          colour={tile.colour}
          width={this.calculateTileSize(width)}
          height={this.calculateTileSize(width)}
        />
      );
    });
  }

  public renderCharacterComponents() {
    const { currentCharacters, previousCharacters, width } = this.props.game;
    return currentCharacters.map((character, index) => {
      character.coordinate = this.getBoardCoordinate(character.coordinate);
      const previousCharacter = previousCharacters.filter(
        c => c.id === character.id,
      )[0];
      const previousCharacterCoordinate = previousCharacter
        ? previousCharacter.coordinate
        : character.coordinate;
      return (
        <PlayerCharacter
          key={index}
          colour={character.colour}
          coordinate={character.coordinate}
          width={this.calculateTileSize(width)}
          height={this.calculateTileSize(width)}
          playerId={character.id}
          previousCoordinate={previousCharacterCoordinate}
        />
      );
    });
  }

  public renderBombComponents() {
    const { bombs, width } = this.props.game;
    return bombs.map((bomb, index) => {
      bomb.coordinate = this.getBoardCoordinate(bomb.coordinate);
      return (
        <Bomb
          key={index}
          bomb={bomb}
          width={this.calculateTileSize(width)}
          height={this.calculateTileSize(width)}
        />
      );
    });
  }

  private getBoardCoordinate(coordinate: Coordinate): Coordinate {
    const { width } = this.props.game;
    const boardCoordinate: Coordinate = { x: 0, y: 0 };
    boardCoordinate.x = coordinate.x * this.calculateTileSize(width);
    boardCoordinate.y = coordinate.y * this.calculateTileSize(width);
    return boardCoordinate;
  }

  private calculateTileSize(width: number) {
    return window.innerWidth / width / 1.7;
  }
}

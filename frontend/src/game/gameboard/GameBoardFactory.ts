import { StandardColors, TileColors } from '../../common/Constants';
import { Character, CharacterInfo, Coordinate, Game, GameMap, PowerUp, Tile, TileType } from '../type';

const colours = ['#4286f4', '#d3422c', '#88d852', '#f0fc0c', '#c774f2'];

export default class GameBoardFactory {
  private gameMap = {} as GameMap;
  private readonly currentTiles = new Map<string, Tile>();
  private currentCharacters: Character[] = [];
  private previousCharacters: Character[] = [];

  private createTiles() {
    const newTiles = this.currentTiles;
    this.addObstacleTiles(this.gameMap.obstaclePositions, newTiles);
    this.addColouredTilesForPlayers(this.gameMap.characterInfos, newTiles);

    return newTiles;
  }

  private createCharacters(): Character[] {
    this.previousCharacters = this.currentCharacters;
    const characters: Character[] = [];
    this.gameMap.characterInfos.forEach((characterInfo, index) => {
      const character = {
        colour: colours[index],
        id: characterInfo.id,
        name: characterInfo.name,
        points: characterInfo.points,
        coordinate: this.getCoordinateFromMapPosition(characterInfo.position),
      };
      characters.push(character);
    });
    this.currentCharacters = characters;
    return characters;
  }

  private createPowerUps() {
    const bombs: PowerUp[] = [];
    this.gameMap.bombPositions.forEach(bombPosition => {
      const bomb = {} as PowerUp;
      bomb.coordinate = this.getCoordinateFromMapPosition(bombPosition);
      bomb.image = '/images/bomb.png';
      bombs.push(bomb);
    });
    return bombs;
  }

  private getPreviousCharacters() {
    return this.previousCharacters;
  }

  private getWidth() {
    return this.gameMap ? this.gameMap.width : 0;
  }

  private getHeight() {
    return this.gameMap ? this.gameMap.height : 0;
  }

  private getWorldTick() {
    return this.gameMap ? this.gameMap.worldTick : 0;
  }

  private addColouredTilesForPlayers(characterInfos: CharacterInfo[], tiles: Map<string, Tile>): void {
    characterInfos.forEach(character => {
      this.addColouredTilesForPlayer(character, tiles);
    });
  }

  private addColouredTilesForPlayer(character: CharacterInfo, tiles: Map<string, Tile>): void {
    character.colouredPositions.forEach(colouredPosition => {
      const colouredTile = {} as Tile;
      colouredTile.coordinate = this.getCoordinateFromMapPosition(colouredPosition);

      colouredTile.type = TileType.COLOURED;
      const theCharacter = this.currentCharacters.filter(c => c.id === character.id)[0];
      colouredTile.colour = theCharacter ? theCharacter.colour : StandardColors.White;

      tiles.set(JSON.stringify(colouredTile.coordinate), colouredTile);
    });
  }

  private addObstacleTiles(obstaclePositions: number[], tiles: Map<string, Tile>): void {
    obstaclePositions.forEach(bombPosition => {
      const obstacleTile = {} as Tile;
      obstacleTile.coordinate = this.getCoordinateFromMapPosition(bombPosition);
      obstacleTile.type = TileType.OBSTACLE;
      obstacleTile.colour = TileColors.Obstacle;
      tiles.set(JSON.stringify(obstacleTile.coordinate), obstacleTile);
    });
  }

  private getCoordinateFromMapPosition(position: number): Coordinate {
    const yPosition = Math.floor(position / this.gameMap.width);
    const xPosition = position - yPosition * this.gameMap.width;
    return { x: xPosition, y: yPosition } as Coordinate;
  }

  getGameBoard(gameMap: GameMap): Game {
    this.gameMap = gameMap;

    const game = {
      tiles: this.createTiles(),
      currentCharacters: this.createCharacters(),
      previousCharacters: this.getPreviousCharacters(),
      bombs: this.createPowerUps(),
      worldTick: this.getWorldTick(),
      width: this.getWidth(),
      height: this.getHeight(),
    };

    return game;
  }
}

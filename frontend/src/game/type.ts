export interface Tile {
  type: TileType;
  colour: string;
  coordinate: Coordinate;
}
// TODO Remove this enum and all use of it. Not needed anymore. Empty tiles no longer exists, and the only
// difference betweeen a coloured tile and an obstacle tile is its color
export enum TileType {
  EMPTY = 'empty-tile',
  COLOURED = 'coloured-tile',
  OBSTACLE = 'obstacle-tile',
}

export interface Coordinate {
  x: number;
  y: number;
}

export interface GameSettings {
  timeInMsPerTick: number;
}

export interface Game {
  tiles: Map<string, Tile>;
  currentCharacters: Character[];
  previousCharacters: Character[];
  bombs: PowerUp[];
  worldTick: number;
  width: number;
  height: number;
}

export interface GameState {
  gameId: string;
  gameTick: number;
  map: GameMap;
  receivingPlayerId: string;
  timeStamp: number;
  type: EventType;
}

export interface GameMap {
  width: number;
  height: number;
  bombPositions: number[];
  obstaclePositions: number[];
  characterInfos: CharacterInfo[];
  worldTick: number;
}

export interface CharacterInfo {
  id: string;
  name: string;
  points: number;
  position: number;
  colouredPositions: number[];
  stunnedForGameTicks: number;
}

export interface Character {
  id: string;
  name: string;
  points: number;
  colour: string;
  coordinate: Coordinate;
}

export interface PowerUp {
  coordinate: Coordinate;
  image: string;
}

export enum EventType {
  GAME_STARTING_EVENT = 'se.cygni.snake.api.event.GameStartingEvent',
  GAME_UPDATE_EVENT = 'se.cygni.snake.api.event.MapUpdateEvent',
  GAME_RESULT_EVENT = 'se.cygni.snake.api.event.GameResultEvent',
  GAME_ENDED_EVENT = 'se.cygni.snake.api.event.GameEndedEvent',
}

declare interface Tile {
  type: TileType;
  colour: string;
  coordinate: Coordinate;
}

enum TileType {
  EMPTY = 'empty-tile',
  COLOURED = 'coloured-tile',
  OBSTACLE = 'obstacle-tile',
}

declare interface Coordinate {
  x: number;
  y: number;
}

declare interface GameState {
  gameId: string;
  gameTick: number;
  map: GameMap;
  receivingPlayerId: string;
  timeStamp: number;
  type: EventType;
}

declare interface GameMap {
  width: number;
  height: number;
  bombPositions: number[];
  obstaclePositions: number[];
  characterInfos: CharacterInfo[];
  worldTick: number;
}

declare interface CharacterInfo {
  id: string;
  name: string;
  points: number;
  position: number;
  colouredPositions: number[];
  stunnedForGameTicks: number;
}

declare interface Character {
  id: string;
  name: string;
  points: number;
  colour: string;
  coordinate: Coordinate;
}

declare interface PowerUp {
  coordinate: Coordinate;
  image: string;
}

enum EventType {
  MAP_UPDATE_EVENT = 'se.cygni.snake.api.event.MapUpdateEvent',
  GAME_ENDED_EVENT = 'se.cygni.snake.api.event.GameEndedEvent',
}

export {
  Tile,
  TileType,
  PowerUp,
  Coordinate,
  GameState,
  GameMap,
  CharacterInfo,
  Character,
  EventType,
};

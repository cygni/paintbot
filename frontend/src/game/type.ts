declare interface Tile {
  type: TileType;
  colour: string;
  coordinate: Coordinate;
}
// TODO Remove this enum and all use of it. Not needed anymore. Empty tiles no longer exists, and the only
// difference betweeen a coloured tile and an obstacle tile is its color
enum TileType {
  EMPTY = 'empty-tile',
  COLOURED = 'coloured-tile',
  OBSTACLE = 'obstacle-tile',
}

declare interface Coordinate {
  x: number;
  y: number;
}

declare interface GameSettings {
  timeInMsPerTick: number;
}

declare interface Game {
  tiles: Map<string, Tile>;
  currentCharacters: Character[];
  previousCharacters: Character[];
  bombs: PowerUp[];
  worldTick: number;
  width: number;
  height: number;
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
  GAME_STARTING_EVENT = 'se.cygni.snake.api.event.GameStartingEvent',
  GAME_UPDATE_EVENT = 'se.cygni.snake.api.event.MapUpdateEvent',
  GAME_RESULT_EVENT = 'se.cygni.snake.api.event.GameResultEvent',
  GAME_ENDED_EVENT = 'se.cygni.snake.api.event.GameEndedEvent',
}

export {
  Tile,
  TileType,
  PowerUp,
  Coordinate,
  Game,
  GameSettings,
  GameState,
  GameMap,
  CharacterInfo,
  Character,
  EventType,
};

declare interface ITile {
    type: TileType
}

declare enum TileType {
    EMPTY = 'empty-tile',
    CHARACTER = 'character-tile',
    OBSTACLE = 'obstacle-tile',
    BOMB = 'bomb-tile',
}

declare interface IGameState {
    gameId: string,
    gameTick: number,
    map: IGameMap,
    receivingPlayerId: string,
    timeStamp: number,
    type: EventType
}

declare interface IGameMap {
    width: number,
    height: number,
    bombPositions: [number],
    obstaclePositions: [number],
    characterInfo: [IPlayerInfo],
    worldTick: number
}

declare interface IPlayerInfo {
    id: string,
    name: string,
    points: number,
    positions: [number],
    stunnedForGameTicks: number
}

declare enum EventType {
    MAP_UPDATE_EVENT = 'se.cygni.snake.api.event.MapUpdateEvent'
}

export { ITile, TileType, IGameState };

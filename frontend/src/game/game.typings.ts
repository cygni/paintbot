declare interface ITile {
    type: TileType,
    color: string | undefined
    coordinate: ICoordinate,
    colorOfInhabitingPlayer: string | undefined
}

enum TileType {
    EMPTY = 'empty-tile',
    CHARACTER = 'character-tile',
    OBSTACLE = 'obstacle-tile',
    BOMB = 'bomb-tile',
}

declare interface ICoordinate {
    x: number,
    y: number
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
    characterInfos: [ICharacterInfo],
    worldTick: number
}

declare interface ICharacterInfo {
    id: string,
    name: string,
    points: number,
    position: number
    colouredPositions: [number],
    stunnedForGameTicks: number
}

declare interface IPlayer {
    id: string,
    name: string,
    points: number, 
    colour: string
}

declare enum EventType {
    MAP_UPDATE_EVENT = 'se.cygni.snake.api.event.MapUpdateEvent'
}

export { ITile, TileType, ICoordinate, IGameState, IGameMap, ICharacterInfo, IPlayer, EventType };

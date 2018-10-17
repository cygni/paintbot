declare interface ITile {
    type: TileType,
    colour: string,
    coordinate: ICoordinate,
}

enum TileType {
    EMPTY = 'empty-tile',
    COLOURED = 'coloured-tile',
    OBSTACLE = 'obstacle-tile',
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

declare interface IClientGameState {
    tiles: Map<string, ITile>
    characters: Map<string, ICharacter>
    bombs: IBomb[],
    width: number,
    height: number,
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

declare interface ICharacter {
    id: string,
    name: string,
    points: number, 
    colour: string,
    coordinate: ICoordinate
}

declare interface IBomb {
    coordinate: ICoordinate,
    image: string
}

enum EventType {
    MAP_UPDATE_EVENT = 'se.cygni.snake.api.event.MapUpdateEvent',
    GAME_ENDED_EVENT = 'se.cygni.snake.api.event.GameEndedEvent',
}

export { ITile, TileType, IBomb, IClientGameState, ICoordinate, IGameState, IGameMap, ICharacterInfo, ICharacter, EventType };

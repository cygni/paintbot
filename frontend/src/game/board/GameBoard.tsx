import * as React from 'react';
// import Blob from '../game-components/Blob';
// import Paint from '../game-components/Paint';
import { ICoordinate, ITile } from '../game.typings';

// import Tile from '../tile/Tile';

interface IProps {
    tiles: Map<ICoordinate, ITile>
}

const GameBoard = (props: IProps) => {
    // const tiles = props.tiles as Map<ICoordinate, ITile>;
    if (props.tiles && props.tiles.size > 0) {
        return (
            <div>hello</div>
        );
    }
    return null;
};

export default GameBoard;
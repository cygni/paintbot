import * as React from 'react';
import Blob from '../game-components/Blob';
import Paint from '../game-components/Paint';
// import { ITile } from '../game.typings';

// import Tile from '../tile/Tile';

const GameBoard = (props: any) => {
    return (
        <div>
                <Blob color={"#2196f3"} rotation={"90deg"}/>
                <Blob color={"#f44336"} rotation={"180deg"}/>
                <Blob color={"#ffeb3b"} rotation={"0deg"}/>
                <Blob color={"#4caf50"} rotation={"270deg"}/>
                <Paint color={"#4caf50"}/>
                <Paint color={"#4caf50"}/>
                <Paint color={"#4caf50"}/>
                <Paint color={"#4caf50"}/>
        </div>
        // props.tiles.map((tile: ITile, index: number) => <Tile key={index} type={tile.type}/>)
    );
};

export default GameBoard;
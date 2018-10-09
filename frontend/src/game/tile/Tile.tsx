import * as React from 'react';
import Blob from '../game-components/Blob';
import { ITile, TileType } from '../game.typings';

interface IProps {
    key: number,
    tile: ITile
}

const Tile = (props: IProps) => {
    let tileInhabitant
    let tiletype;
    if (props.tile && props.tile.type ===  TileType.CHARACTER) {
        tileInhabitant = (<Blob color={props.tile.colourOfInhabitingPlayer || "black"} rotation={"90deg"}/>)
        tiletype = TileType.CHARACTER.valueOf();
    }
    if(tiletype) {
        return (
            <div className={tiletype}>
                <div>
                    {tileInhabitant}
                </div>
            </div>
        )
    }

    return null;
}

export default Tile;
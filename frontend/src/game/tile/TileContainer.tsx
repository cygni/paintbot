import * as React from 'react';
import { ITile, TileType } from '../game.typings';
import StandardTile from './StandardTile';

interface IProps {
    key: number,
    tile: ITile,
    width: number,
    height: number,
}

interface IState {
    height: number,
    tile: ITile,
    width: number
}

export default class TileContainer extends React.Component<IProps, IState> {

    public render() {
        let fillColour;
        if(this.props.tile.type === TileType.EMPTY) {
            fillColour = '#eff2f7';
        }
        if(this.props.tile.type === TileType.COLOURED) {
            fillColour = this.props.tile.colour;
        }
        if(this.props.tile.type === TileType.OBSTACLE) {
            fillColour = '#041126'
        }
        return <StandardTile colour = {fillColour || 'white'} coordinate = {this.props.tile.coordinate} width = {this.props.width} height={this.props.height}/>
    }
}
import * as React from 'react';
import { ITile, TileType } from '../game.typings';
import BombTile from './BombTile';
import CharacterTile from './CharacterTile';
import ColouredTile from './ColouredTile';
import EmptyTile from './EmptyTile';

interface IProps {
    key: number,
    tile: ITile,
    width: number,
    height: number,
    playerId: string
}

interface IState {
    height: number,
    tile: ITile,
    width: number
}

export default class Tile extends React.Component<IProps, IState> {

    constructor(props: IProps) {
        super(props);

        this.state = {
            height: this.props.height,
            tile: this.props.tile,
            width: this.props.width,
        }
    }

    public componentDidUpdate(prevProps: IProps, prevState: IState) {
        if(this.props.tile !== prevState.tile) {
            this.setState({ tile: this.props.tile });
        }
    }

    public render() {
        if(this.state && this.state.tile) {
            const fillColor = this.state.tile.colour || this.state.tile.colourOfInhabitingPlayer;
            if(this.state.tile.type === TileType.CHARACTER) {
                return <CharacterTile colour={fillColor} coordinate={this.state.tile.coordinate} width={this.state.width} height={this.state.height} playerId={this.props.playerId}/>
            }
            if(this.state.tile.type === TileType.OBSTACLE) {
                return <ColouredTile colour = {'#041126'} coordinate = {this.state.tile.coordinate} width = {this.state.width} height={this.state.height} opacity={1}/>
            }
            if(this.state.tile.type === TileType.BOMB) {
                return <BombTile  coordinate = {this.state.tile.coordinate} width = {this.state.width} height={this.state.height}/>
            }
            if(this.state.tile.type === TileType.EMPTY) {
                if(this.state.tile.isColoured) {
                    return <ColouredTile colour = {fillColor} coordinate = {this.state.tile.coordinate} width = {this.state.width} height={this.state.height} opacity={1}/>
                }
                return <EmptyTile coordinate = {this.state.tile.coordinate} width = {this.state.width} height={this.state.height}/>
            }
        }
        return null;
    }
}
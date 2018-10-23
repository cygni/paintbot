import * as React from 'react';
import FlipMove from 'react-flip-move';
import styled from 'styled-components';
import { ScoreBoardColors } from '../../common/Constants';
import { Header } from '../../common/Header';
import { Indent } from '../../common/Indent';
import { Character } from '../type';
import ScoreBoardEntry from './ScoreBoardEntry';
import { SortOrder, sortPlayers } from './Util';

interface Props {
  players: Character[];
  worldTick?: number
}


const Container = styled.div`
  text-align: center;
  background-color: ${ScoreBoardColors.Background};
  margin-left: 10px;
  width: 300px;
`;

export default class ScoreBoardContainer extends React.Component<Props> {

  public shouldComponentUpdate(nextProps: Props) {
    return !!nextProps.worldTick && (nextProps.worldTick % 5 === 0 || nextProps.worldTick === 1);
  }

  public render() {
    return (
      <Container>
        <Header label={'Score'} />
        <Indent>
          <FlipMove>{this.getPlayers()}</FlipMove>
        </Indent>
      </Container>
    );
  }

  private getPlayers() {
    const sortedPlayers = sortPlayers(this.props.players, SortOrder.DESCENDING);
    return sortedPlayers.map((player, index) => {
      return <ScoreBoardEntry key={player.id} player={player} />;
    });
  }
}

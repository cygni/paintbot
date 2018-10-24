import * as React from 'react';
import * as FlipMove from 'react-flip-move';
import styled from 'styled-components';
import { ScoreBoardColors, StandardColors } from '../../common/Constants';
import { Indent } from '../../common/Indent';
import { Row } from '../../common/Row';
import { Character } from '../type';
import ScoreBoardEntry from './ScoreBoardEntry';
import { SortOrder, sortPlayers } from './Util';

interface Props {
  players: Character[];
  worldTick?: number;
}

const Container = styled.div`
  text-align: center;
  background-color: ${ScoreBoardColors.Background};
  margin-left: 10px;
  display: inline-block;
  max-width: 400px;
`;

export default class ScoreBoardContainer extends React.Component<Props> {
  public shouldComponentUpdate(nextProps: Props) {
    return (
      !!nextProps.worldTick &&
      (nextProps.worldTick % 5 === 0 || nextProps.worldTick === 1)
    );
  }

  public render() {
    return (
      <Container>
        <Row style={{ backgroundColor: StandardColors.Black }}>
          <h1>Score</h1>
        </Row>
        <Indent>
          <FlipMove>{this.getPlayers()}</FlipMove>
        </Indent>
      </Container>
    );
  }

  private getPlayers() {
    const sortedPlayers = sortPlayers(this.props.players, SortOrder.DESCENDING);
    return sortedPlayers.map(player => {
      return <ScoreBoardEntry key={player.id} player={player} />;
    });
  }
}

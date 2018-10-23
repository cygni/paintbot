import * as React from 'react';
import styled from 'styled-components';
import { StandardColors } from '../../common/Constants';
import { Row } from '../../common/Row';
import { Spacing } from '../../common/Spacing';
import { TextLabel } from '../../common/TextLabel';
import { Character } from '../type';

interface Props {
  player: Character;
}

interface ScoreLabelContainer {
  playerColour: string;
}

const ScoreLabelContainer = styled.div`
  opacity: 1;
  color: ${(props: ScoreLabelContainer) => props.playerColour};
  font-size: 32px;
`;

export default class ScoreBoardEntry extends React.Component<Props> {
  public shouldComponentUpdate(nextProps: Props) {
    return nextProps.player.points !== this.props.player.points;
  }

  public render() {
    const { player } = this.props;
    const playerName = player.name;
    const playerScore = player.points;
    return (
      <ScoreLabelContainer playerColour={player.colour}>
        <Spacing num={3}>
          <Row
            justifyContent={'space-between'}
            style={{
              display: 'flex',
              backgroundColor: StandardColors.Black,
              borderRadius: '5px',
            }}
          >
            <TextLabel
              style={{
                fontWeight: 'bold',
                padding: '10px',
              }}
            >
              {playerName}
            </TextLabel>
            <TextLabel
              style={{
                fontWeight: 'bold',
                padding: '10px',
              }}
            >
              {`${playerScore}`}
            </TextLabel>
          </Row>
        </Spacing>
      </ScoreLabelContainer>
    );
  }
}

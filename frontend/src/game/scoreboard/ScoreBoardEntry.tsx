import * as React from 'react';
import styled from 'styled-components';
import { StandardColors } from '../../common/Constants';
import { DefaultText } from '../../common/DefaultText';
import { Row } from '../../common/Row';
import { Spacing } from '../../common/Spacing';
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
  transition: position 0.5s linear;
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
            <DefaultText
              style={{
                fontWeight: 'bold',
                padding: '10px',
              }}
            >
              {playerName}
            </DefaultText>
            <DefaultText
              style={{
                fontWeight: 'bold',
                padding: '10px',
              }}
            >
              {`${playerScore}`}
            </DefaultText>
          </Row>
        </Spacing>
      </ScoreLabelContainer>
    );
  }
}

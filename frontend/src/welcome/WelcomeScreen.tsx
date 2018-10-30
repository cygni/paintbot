import * as React from 'react';
import { RouteComponentProps } from 'react-router-dom';
import styled from 'styled-components';
import { StandardColors } from '../common/Constants';
import { HoverButton } from '../common/ui/HoverButton';
import { Indent } from '../common/ui/Indent';
import { Row } from '../common/ui/Row';
import { Spacing } from '../common/ui/Spacing';
import * as background from '../resources/background.jpg';
import * as introImage from './images/welcome.png';
import * as yellowCharacter from './images/yellow_character.png';

export default class WelcomeScreen extends React.Component<
  RouteComponentProps
> {
  public render() {
    return (
      <Container>
        <MenuContainer>
          <Row justifyContent={'space-between'}>
            <Spacing>
              <Row>
                <HeaderText>FÄRGLANDET</HeaderText>
                <YellowCharacter src={yellowCharacter} />
              </Row>
            </Spacing>
            <Spacing>
              <Indent num={2}>
                <HoverButton onClick={this.startGame}>Start</HoverButton>
              </Indent>
            </Spacing>
          </Row>
        </MenuContainer>
        <BodyContainer>
          <ComicImage src={introImage} />
        </BodyContainer>
      </Container>
    );
  }

  private startGame = () => {
    this.props.history.push('/game');
  };
}

const Container = styled.div`
  background-image: url(${background});
  position: absolute;
  background-repeat: no-repeat;
  background-size: cover;
  top: 0;
  right: 0;
  bottom: 0;
  left: 0;
`;

const MenuContainer = styled.div`
  line-height: 50px;
  background-color: rgba(0, 0, 0, 0.1);
  box-shadow: 0 4px 8px 0 rgba(0, 0, 0, 0.2), 0 6px 20px 0 rgba(0, 0, 0, 0.19);
`;

const HeaderText = styled.span`
  font-size: 30px;
  font-weight: bold;
  height: 100%;
  padding-left: 20px;
  color: ${StandardColors.White};
`;

const BodyContainer = styled.div`
  margin-top: 20px;
  display: flex;
  justify-content: center;
`;

const ComicImage = styled.img`
  width: 70%;
  height: 70%;
`;

const YellowCharacter = styled.img`
  width: 30px;
  height: 30px;
`;

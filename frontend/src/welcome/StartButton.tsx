import * as React from 'react';
import styled from 'styled-components';
import { CharacterColors, StandardColors } from '../common/Constants';

interface Props {
  color?: string;
  backgroundColor?: string;
  hoverBackgroundColor?: string;
  children?: string;
  onClick?: () => void;
}

const Span = styled.span`
  cursor: pointer;
  display: inline-block;
  position: relative;
  transition: 0.5s;
  &:after {
    content: '\\00bb';
    position: absolute;
    opacity: 0;
    top: 0;
    right: -20px;
    transition: 0.5s;
  }
`;

const Button = styled.button`
  display: inline-block;
  border-radius: 4px;
  background-color: ${CharacterColors.RedCharacter};
  border: none;
  color: ${StandardColors.White};
  text-align: center;
  font-size: 18px;
  padding: 10px;
  width: 200px;
  transition: all 0.5s;
  cursor: pointer;
  margin: 5px;
  &:hover {
    ${Span} {
      padding-right: 25px;
    }
    ${Span}:after {
      opacity: 1;
      right: 0;
    }
  }
`;

export const StartButton = ({
  color,
  backgroundColor,
  hoverBackgroundColor,
  children,
  onClick,
}: Props) => {
  return (
    <Button onClick={onClick}>
      <Span>{children}</Span>
    </Button>
  );
};

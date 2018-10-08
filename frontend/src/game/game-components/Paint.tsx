import * as React from 'react';
import styled, { keyframes, StyledFunction } from "styled-components"

interface IProps {
    color: string;
};

const paintContainer: StyledFunction<IProps & React.HTMLProps<HTMLInputElement>> = styled.div

const Container = styled.div`
  height: 5rem;
  width: 5rem;
  position: relative;
  display: inline-block;
`

const spread = keyframes`
  from {
    height: 0%;
    width: 0%;
    left: 50%;
    top: 50%;
  }

  to {
    height: 112.5%;
    width: 112.5%;
    left: -5.625%;
    top: -5.625%;
  }
`;

const PaintContainer = paintContainer`
  fill: ${props => props.color || "white"};
  position: absolute;
  height: 112.5%;
  width: 112.5%;
  left: -5.625%;
  top: -5.625%;
  animation: ${spread} 2s ease-out;
`

export default class Paint extends React.Component<IProps> {
    
    constructor(props: IProps) {
        super(props);
    }

    public render() {
        return (
            <Container>
                <PaintContainer color={this.props.color}>
                    <svg x="0px" y="0px" viewBox="0 0 72 72">
                        <path d="M8,68c0,0,3-4,8-4s6.5,8,12.5,8s2.5-8,7.5-8s2.1,8,8,8c5.5,0,5.5-8,11-8s8.5,3.5,9,4s2.5,1.5,4,0s0.5-3.5,0-4
                            s-4-2-4-8s8-4,8-11s-8-2-8-9s8-1.7,8-8s-8-6-8-12s4-8,4-8l-4-4c0,0-3.5-4-9-4s-5.5,8-11,8c-5.9,0-3-8-8-8s-1.5,8-7.5,8S21,0,16,0
                            S7.5,3.5,8,4s1.5,2.5,0,4S4.5,8.5,4,8s-4,2-4,8s8,5.7,8,12s-8,1-8,8s8,2,8,9s-8,5-8,11s4,8,4,8L8,68z"/>
                    </svg>
                </PaintContainer>
            </Container>
        )
    }
}
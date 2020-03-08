import React from 'react';
import styled from 'styled-components';
import oldPaper from './old-paper.jpg';

const Paper = styled.img`
  width: 100%;
  transform: translate(0, -25%);
`;
const PaperWrapper = styled.div`
  max-height: 400px;
  overflow: hidden;
`;

const Overlay = styled.div`
  position: absolute;
  top: 0;
  right: 0;
  font-size: 50px;
  color: black;
  background-color: whitesmoke;
  z-index: 10;
`;

export function Jumbo() {
  return (
    <header>
      <PaperWrapper>
        <Paper src={oldPaper} alt={'Publish tons of papers'} />
      </PaperWrapper>
    </header>
  );
}

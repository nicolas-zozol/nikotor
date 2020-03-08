import React from 'react';
import {FontAwesomeIcon} from '@fortawesome/react-fontawesome';
import {IconDefinition} from '@fortawesome/free-solid-svg-icons';
import styled from 'styled-components';

const StyledFontAwesome = styled(FontAwesomeIcon)``;
const Span = styled.span`
  ${StyledFontAwesome} {
    margin-right: 20px;
    color: rebeccapurple;
  }
  margin: 0 20px;
`;

type SpaceIcon = {
  icon: IconDefinition;
  children: React.ReactNode;
};

export function SpaceIcon({icon, children}: SpaceIcon) {
  return (
    <Span>
      <StyledFontAwesome icon={icon} />
      {children}
    </Span>
  );
}

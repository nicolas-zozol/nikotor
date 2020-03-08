import React from 'react';
import {User} from './api/user.model';

interface ApplicationContextType {
  user?: User;
}

export const ApplicationContext = React.createContext<ApplicationContextType>({
  user: undefined,
});

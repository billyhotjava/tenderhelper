import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import BidInfo from './bid-info';
import BidInfoDetail from './bid-info-detail';
import BidInfoUpdate from './bid-info-update';
import BidInfoDeleteDialog from './bid-info-delete-dialog';

const BidInfoRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<BidInfo />} />
    <Route path="new" element={<BidInfoUpdate />} />
    <Route path=":id">
      <Route index element={<BidInfoDetail />} />
      <Route path="edit" element={<BidInfoUpdate />} />
      <Route path="delete" element={<BidInfoDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default BidInfoRoutes;

import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './bid-info.reducer';

export const BidInfoDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const bidInfoEntity = useAppSelector(state => state.bidInfo.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="bidInfoDetailsHeading">
          <Translate contentKey="tenderhelperApp.bidInfo.detail.title">BidInfo</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{bidInfoEntity.id}</dd>
          <dt>
            <span id="bidPrjId">
              <Translate contentKey="tenderhelperApp.bidInfo.bidPrjId">Bid Prj Id</Translate>
            </span>
          </dt>
          <dd>{bidInfoEntity.bidPrjId}</dd>
          <dt>
            <span id="bidPrjName">
              <Translate contentKey="tenderhelperApp.bidInfo.bidPrjName">Bid Prj Name</Translate>
            </span>
          </dt>
          <dd>{bidInfoEntity.bidPrjName}</dd>
          <dt>
            <span id="bidSectionId">
              <Translate contentKey="tenderhelperApp.bidInfo.bidSectionId">Bid Section Id</Translate>
            </span>
          </dt>
          <dd>{bidInfoEntity.bidSectionId}</dd>
          <dt>
            <span id="bidSection">
              <Translate contentKey="tenderhelperApp.bidInfo.bidSection">Bid Section</Translate>
            </span>
          </dt>
          <dd>{bidInfoEntity.bidSection}</dd>
          <dt>
            <span id="bidder">
              <Translate contentKey="tenderhelperApp.bidInfo.bidder">Bidder</Translate>
            </span>
          </dt>
          <dd>{bidInfoEntity.bidder}</dd>
          <dt>
            <span id="bidPrice">
              <Translate contentKey="tenderhelperApp.bidInfo.bidPrice">Bid Price</Translate>
            </span>
          </dt>
          <dd>{bidInfoEntity.bidPrice}</dd>
          <dt>
            <span id="averageValue">
              <Translate contentKey="tenderhelperApp.bidInfo.averageValue">Average Value</Translate>
            </span>
          </dt>
          <dd>{bidInfoEntity.averageValue}</dd>
          <dt>
            <span id="validPrice">
              <Translate contentKey="tenderhelperApp.bidInfo.validPrice">Valid Price</Translate>
            </span>
          </dt>
          <dd>{bidInfoEntity.validPrice}</dd>
          <dt>
            <span id="validAverageValue">
              <Translate contentKey="tenderhelperApp.bidInfo.validAverageValue">Valid Average Value</Translate>
            </span>
          </dt>
          <dd>{bidInfoEntity.validAverageValue}</dd>
          <dt>
            <span id="declineRatio">
              <Translate contentKey="tenderhelperApp.bidInfo.declineRatio">Decline Ratio</Translate>
            </span>
          </dt>
          <dd>{bidInfoEntity.declineRatio}</dd>
          <dt>
            <span id="basePrice">
              <Translate contentKey="tenderhelperApp.bidInfo.basePrice">Base Price</Translate>
            </span>
          </dt>
          <dd>{bidInfoEntity.basePrice}</dd>
          <dt>
            <span id="benchmarkScore">
              <Translate contentKey="tenderhelperApp.bidInfo.benchmarkScore">Benchmark Score</Translate>
            </span>
          </dt>
          <dd>{bidInfoEntity.benchmarkScore}</dd>
          <dt>
            <span id="ranking">
              <Translate contentKey="tenderhelperApp.bidInfo.ranking">Ranking</Translate>
            </span>
          </dt>
          <dd>{bidInfoEntity.ranking}</dd>
        </dl>
        <Button tag={Link} to="/bid-info" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/bid-info/${bidInfoEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default BidInfoDetail;

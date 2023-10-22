import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IBidInfo } from 'app/shared/model/bid-info.model';
import { getEntity, updateEntity, createEntity, reset } from './bid-info.reducer';

export const BidInfoUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const bidInfoEntity = useAppSelector(state => state.bidInfo.entity);
  const loading = useAppSelector(state => state.bidInfo.loading);
  const updating = useAppSelector(state => state.bidInfo.updating);
  const updateSuccess = useAppSelector(state => state.bidInfo.updateSuccess);

  const handleClose = () => {
    navigate('/bid-info' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...bidInfoEntity,
      ...values,
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {}
      : {
          ...bidInfoEntity,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="tenderhelperApp.bidInfo.home.createOrEditLabel" data-cy="BidInfoCreateUpdateHeading">
            <Translate contentKey="tenderhelperApp.bidInfo.home.createOrEditLabel">Create or edit a BidInfo</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField
                  name="id"
                  required
                  readOnly
                  id="bid-info-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('tenderhelperApp.bidInfo.bidPrjId')}
                id="bid-info-bidPrjId"
                name="bidPrjId"
                data-cy="bidPrjId"
                type="text"
              />
              <ValidatedField
                label={translate('tenderhelperApp.bidInfo.bidPrjName')}
                id="bid-info-bidPrjName"
                name="bidPrjName"
                data-cy="bidPrjName"
                type="text"
              />
              <ValidatedField
                label={translate('tenderhelperApp.bidInfo.bidSectionId')}
                id="bid-info-bidSectionId"
                name="bidSectionId"
                data-cy="bidSectionId"
                type="text"
              />
              <ValidatedField
                label={translate('tenderhelperApp.bidInfo.bidSection')}
                id="bid-info-bidSection"
                name="bidSection"
                data-cy="bidSection"
                type="text"
              />
              <ValidatedField
                label={translate('tenderhelperApp.bidInfo.bidder')}
                id="bid-info-bidder"
                name="bidder"
                data-cy="bidder"
                type="text"
              />
              <ValidatedField
                label={translate('tenderhelperApp.bidInfo.bidPrice')}
                id="bid-info-bidPrice"
                name="bidPrice"
                data-cy="bidPrice"
                type="text"
              />
              <ValidatedField
                label={translate('tenderhelperApp.bidInfo.averageValue')}
                id="bid-info-averageValue"
                name="averageValue"
                data-cy="averageValue"
                type="text"
              />
              <ValidatedField
                label={translate('tenderhelperApp.bidInfo.declineRatio')}
                id="bid-info-declineRatio"
                name="declineRatio"
                data-cy="declineRatio"
                type="text"
              />
              <ValidatedField
                label={translate('tenderhelperApp.bidInfo.basePrice')}
                id="bid-info-basePrice"
                name="basePrice"
                data-cy="basePrice"
                type="text"
              />
              <ValidatedField
                label={translate('tenderhelperApp.bidInfo.benchmarkScore')}
                id="bid-info-benchmarkScore"
                name="benchmarkScore"
                data-cy="benchmarkScore"
                type="text"
              />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/bid-info" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default BidInfoUpdate;

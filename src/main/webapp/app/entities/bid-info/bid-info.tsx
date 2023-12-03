import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate, getPaginationState, JhiPagination, JhiItemCount } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSort, faSortUp, faSortDown } from '@fortawesome/free-solid-svg-icons';
import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/shared/util/pagination.constants';
import { overridePaginationStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities } from './bid-info.reducer';
import './bid-info.scss';

export const BidInfo = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [sectionType, setSectionType] = useState('server');
  const [declineRatio, setDeclineRatio] = useState(0.03);
  const [n1, setN1] = useState(1.4);
  const [n2, setN2] = useState(0.7);

  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getPaginationState(pageLocation, ITEMS_PER_PAGE, 'id'), pageLocation.search),
  );

  type BidInfo = {
    id: number;
    bidPrjId: string;
    bidPrjName: string;
    bidSectionId: string;
    bidSection: string;
    bidder: string;
    bidPrice: number;
    averageValue: number;
    validPrice: number;
    validAverageValue: number;
    declineRatio: number;
    basePrice: number;
    benchmarkScore: number;
    ranking: number;
  };
  const bidInfoList: BidInfo[] = useAppSelector(state => state.bidInfo.entities);
  const loading = useAppSelector(state => state.bidInfo.loading);
  const totalItems = useAppSelector(state => state.bidInfo.totalItems);

  const getAllEntities = () => {
    dispatch(
      getEntities({
        page: paginationState.activePage - 1,
        size: paginationState.itemsPerPage,
        sort: `${paginationState.sort},${paginationState.order}`,
      }),
    );
  };

  const sortEntities = () => {
    getAllEntities();
    const endURL = `?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`;
    if (pageLocation.search !== endURL) {
      navigate(`${pageLocation.pathname}${endURL}`);
    }
  };

  useEffect(() => {
    sortEntities();
  }, [paginationState.activePage, paginationState.order, paginationState.sort]);

  useEffect(() => {
    const params = new URLSearchParams(pageLocation.search);
    const page = params.get('page');
    const sort = params.get(SORT);
    if (page && sort) {
      const sortSplit = sort.split(',');
      setPaginationState({
        ...paginationState,
        activePage: +page,
        sort: sortSplit[0],
        order: sortSplit[1],
      });
    }
  }, [pageLocation.search]);

  const sort = p => () => {
    setPaginationState({
      ...paginationState,
      order: paginationState.order === ASC ? DESC : ASC,
      sort: p,
    });
  };

  const handlePagination = currentPage =>
    setPaginationState({
      ...paginationState,
      activePage: currentPage,
    });

  const handleSyncList = () => {
    sortEntities();
  };

  const getSortIconByFieldName = (fieldName: string) => {
    const sortFieldName = paginationState.sort;
    const order = paginationState.order;
    if (sortFieldName !== fieldName) {
      return faSort;
    } else {
      return order === ASC ? faSortUp : faSortDown;
    }
  };

  const startCompute = async () => {
    const requestData = {
      sectionType,
      declineRatio,
      n1,
      n2,
    };
    try {
      const response = await fetch('/api/bid-infos/batch-computing', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(requestData),
      });
      if (!response.ok) {
        throw new Error('Network response was not ok');
      }
      await response.json(); // 如果服务器返回了json数据
    } catch (error) {
      // console.error("Error starting compute:", error);
    }
    handleSyncList();
  };

  const handleStartCompute = () => {
    startCompute();
  };

  const deleteAllData = async () => {
    try {
      const response = await fetch('/api/bid-infos/deleteAllData', {
        method: 'DELETE',
        // 如果有其他配置，如headers、body等，也可以在此添加
      });

      if (!response.ok) {
        throw new Error('Network response was not ok');
      }
      await response.json(); // 如果服务器返回了json数据
    } catch (error) {
      // console.error("Error starting compute:", error);
    }

    // 删除成功后，调用handleSyncList来刷新Table数据
    handleSyncList();
  };

  const handleDeleteAllData = () => {
    deleteAllData();
  };

  // 获取所有的bidSectionId作为下拉框的选项（确保是唯一的）
  const [selectedBidSectionId, setSelectedBidSectionId] = useState('');
  const uniqueBidSectionIds = [...new Set(bidInfoList.map(info => info.bidSectionId))];
  const handleSelectionChange = (event: React.ChangeEvent<HTMLSelectElement>) => {
    setSelectedBidSectionId(event.target.value);
  };

  const filteredBidInfoList = selectedBidSectionId ? bidInfoList.filter(info => info.bidSectionId === selectedBidSectionId) : bidInfoList;

  return (
    <div>
      <h2 id="bid-info-heading" data-cy="BidInfoHeading">
        <Translate contentKey="tenderhelperApp.bidInfo.home.title">Bid Infos</Translate>
        <div className="d-flex justify-content-center">
          {/* Server Dropdown */}
          <label className="mylabel" htmlFor="serverSelect">
            <Translate contentKey="tenderhelperApp.bidInfo.sectionType">Section Type</Translate>:
          </label>
          <select className="myselect mr-3" id="serverSelect" value={sectionType} onChange={e => setSectionType(e.target.value)}>
            <option value="server">
              <Translate contentKey="tenderhelperApp.bidInfo.sectionTypeServer">Server</Translate>
            </option>
            <option value="storage">
              <Translate contentKey="tenderhelperApp.bidInfo.sectionTypeStorage">Storage</Translate>
            </option>
          </select>
          &nbsp; &nbsp;
          {/* Ratio Dropdown */}
          <label className="mylabel" htmlFor="ratioSelect">
            <Translate contentKey="tenderhelperApp.bidInfo.declineRatio">decline ratio</Translate>:
          </label>
          <select
            className="myselect mr-3"
            id="ratioSelect"
            value={declineRatio}
            onChange={e => setDeclineRatio(parseFloat(e.target.value))}
          >
            <option value="0.03">3%</option>
            <option value="0.04">4%</option>
            <option value="0.05">5%</option>
            <option value="0.06">6%</option>
          </select>
          &nbsp; &nbsp;
          {/* n1 Dropdown */}
          <label className="mylabel" htmlFor="n1Select">
            <Translate contentKey="tenderhelperApp.bidInfo.n1Value">n1Value</Translate>:
          </label>
          <select className="myselect mr-3" id="n1Select" value={n1} onChange={e => setN1(parseFloat(e.target.value))}>
            <option value="1.4">1.4</option>
          </select>
          &nbsp; &nbsp;
          {/* n2 Dropdown */}
          <label className="mylabel" htmlFor="n2Select">
            <Translate contentKey="tenderhelperApp.bidInfo.n2Value">n2Value</Translate>:
          </label>
          <select className="myselect mr-3" id="n2Select" value={n2} onChange={e => setN2(parseFloat(e.target.value))}>
            <option value="0.7">0.7</option>
          </select>
          &nbsp; &nbsp;
          <Button color="primary" onClick={handleStartCompute}>
            <Translate contentKey="tenderhelperApp.bidInfo.home.oneKeyCompute">start to compute</Translate>
          </Button>
          &nbsp; &nbsp;
          <Button color="primary" onClick={handleDeleteAllData}>
            <Translate contentKey="tenderhelperApp.bidInfo.home.deleteAllData">delete all data</Translate>
          </Button>
          &nbsp; &nbsp;
          <select className="mylabel" value={selectedBidSectionId} onChange={handleSelectionChange}>
            <option value="">
              <Translate contentKey="tenderhelperApp.bidInfo.home.allSectionID">Select a bidSectionId</Translate>
            </option>
            {uniqueBidSectionIds.map(bidSectionId => (
              <option key={bidSectionId} value={bidSectionId}>
                {bidSectionId}
              </option>
            ))}
          </select>
        </div>

        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="tenderhelperApp.bidInfo.home.refreshListLabel">Refresh List</Translate>
          </Button>
          &nbsp; &nbsp;
          <Link to="/bid-info/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="tenderhelperApp.bidInfo.home.createLabel">Create new Bid Info</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {bidInfoList && bidInfoList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="tenderhelperApp.bidInfo.id">ID</Translate> <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('bidPrjId')}>
                  <Translate contentKey="tenderhelperApp.bidInfo.bidPrjId">Bid Prj Id</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('bidPrjId')} />
                </th>
                <th className="hand" onClick={sort('bidPrjName')}>
                  <Translate contentKey="tenderhelperApp.bidInfo.bidPrjName">Bid Prj Name</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('bidPrjName')} />
                </th>
                <th className="hand" onClick={sort('bidSectionId')}>
                  <Translate contentKey="tenderhelperApp.bidInfo.bidSectionId">Bid Section Id</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('bidSectionId')} />
                </th>
                <th className="hand" onClick={sort('bidSection')}>
                  <Translate contentKey="tenderhelperApp.bidInfo.bidSection">Bid Section</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('bidSection')} />
                </th>
                <th className="hand" onClick={sort('bidder')}>
                  <Translate contentKey="tenderhelperApp.bidInfo.bidder">Bidder</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('bidder')} />
                </th>
                <th className="hand" onClick={sort('bidPrice')}>
                  <Translate contentKey="tenderhelperApp.bidInfo.bidPrice">Bid Price</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('bidPrice')} />
                </th>
                <th className="hand" onClick={sort('averageValue')}>
                  <Translate contentKey="tenderhelperApp.bidInfo.averageValue">Average Value</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('averageValue')} />
                </th>
                <th className="hand" onClick={sort('validPrice')}>
                  <Translate contentKey="tenderhelperApp.bidInfo.validPrice">Valid Price</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('validPrice')} />
                </th>
                <th className="hand" onClick={sort('validAverageValue')}>
                  <Translate contentKey="tenderhelperApp.bidInfo.validAverageValue">Valid Average Value</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('validAverageValue')} />
                </th>
                <th className="hand" onClick={sort('declineRatio')}>
                  <Translate contentKey="tenderhelperApp.bidInfo.declineRatio">Decline Ratio</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('declineRatio')} />
                </th>
                <th className="hand" onClick={sort('basePrice')}>
                  <Translate contentKey="tenderhelperApp.bidInfo.basePrice">Base Price</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('basePrice')} />
                </th>
                <th className="hand" onClick={sort('benchmarkScore')}>
                  <Translate contentKey="tenderhelperApp.bidInfo.benchmarkScore">Benchmark Score</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('benchmarkScore')} />
                </th>
                <th className="hand" onClick={sort('ranking')}>
                  <Translate contentKey="tenderhelperApp.bidInfo.ranking">Ranking</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('ranking')} />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {filteredBidInfoList.map((bidInfo, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/bid-info/${bidInfo.id}`} color="link" size="sm">
                      {bidInfo.id}
                    </Button>
                  </td>
                  <td>{bidInfo.bidPrjId}</td>
                  <td>{bidInfo.bidPrjName}</td>
                  <td>{bidInfo.bidSectionId}</td>
                  <td>{bidInfo.bidSection}</td>
                  <td>{bidInfo.bidder}</td>
                  <td>{Number(bidInfo.bidPrice).toFixed(6)}</td>
                  <td>{Number(bidInfo.averageValue).toFixed(6)}</td>
                  <td>{bidInfo.validPrice}</td>
                  <td>{bidInfo.validAverageValue}</td>
                  <td>{bidInfo.declineRatio}</td>
                  <td>{Number(bidInfo.basePrice).toFixed(6)}</td>
                  <td>{bidInfo.benchmarkScore}</td>
                  <td>{bidInfo.ranking}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/bid-info/${bidInfo.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/bid-info/${bidInfo.id}/edit?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
                        color="primary"
                        size="sm"
                        data-cy="entityEditButton"
                      >
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/bid-info/${bidInfo.id}/delete?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
                        color="danger"
                        size="sm"
                        data-cy="entityDeleteButton"
                      >
                        <FontAwesomeIcon icon="trash" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.delete">Delete</Translate>
                        </span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && (
            <div className="alert alert-warning">
              <Translate contentKey="tenderhelperApp.bidInfo.home.notFound">No Bid Infos found</Translate>
            </div>
          )
        )}
      </div>
      {totalItems ? (
        <div className={bidInfoList && bidInfoList.length > 0 ? '' : 'd-none'}>
          <div className="justify-content-center d-flex">
            <JhiItemCount page={paginationState.activePage} total={totalItems} itemsPerPage={paginationState.itemsPerPage} i18nEnabled />
          </div>
          <div className="justify-content-center d-flex">
            <JhiPagination
              activePage={paginationState.activePage}
              onSelect={handlePagination}
              maxButtons={5}
              itemsPerPage={paginationState.itemsPerPage}
              totalItems={totalItems}
            />
          </div>
        </div>
      ) : (
        ''
      )}
    </div>
  );
};

export default BidInfo;

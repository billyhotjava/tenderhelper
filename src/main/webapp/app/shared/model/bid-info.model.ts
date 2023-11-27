export interface IBidInfo {
  id?: number;
  bidPrjId?: string | null;
  bidPrjName?: string | null;
  bidSectionId?: string | null;
  bidSection?: string | null;
  bidder?: string | null;
  bidPrice?: number | null;
  averageValue?: number | null;
  validPrice?: number | null;
  validAverageValue?: number | null;
  declineRatio?: number | null;
  basePrice?: number | null;
  benchmarkScore?: number | null;
  ranking?: number | null;
}

export const defaultValue: Readonly<IBidInfo> = {};

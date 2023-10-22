export interface IBidInfo {
  id?: number;
  bidPrjId?: string | null;
  bidPrjName?: string | null;
  bidSectionId?: string | null;
  bidSection?: string | null;
  bidder?: string | null;
  bidPrice?: number | null;
  averageValue?: number | null;
  declineRatio?: number | null;
  basePrice?: number | null;
  benchmarkScore?: number | null;
}

export const defaultValue: Readonly<IBidInfo> = {};
